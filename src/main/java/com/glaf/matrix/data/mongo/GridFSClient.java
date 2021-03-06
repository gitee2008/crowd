/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.glaf.matrix.data.mongo;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.Adler32;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.imgscalr.Scalr;

import com.glaf.matrix.data.mongo.object.MongoDBDriver;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

public class GridFSClient extends MongoDBClient {

	private static volatile GridFS _gridFS = null;

	private Object lock = new Object();

	protected static final String[] IMAGE_FORMAT = { "jpg", "jpeg", "png" };

	public void setMongoDBDriver(MongoDBDriver mongoDBDriver) {
		this.mongoDBDriver = mongoDBDriver;
	}

	public GridFS getInstance() {
		if (_gridFS != null) {
			return _gridFS;
		}
		synchronized (lock) {
			if (_gridFS != null) {
				return _gridFS;
			}
			_gridFS = new GridFS(mongoDBDriver.getDB(this.databaseName));
			return _gridFS;
		}
	}

	public void close() {
		mongoDBDriver.close();
	}

	/**
	 *
	 * @param inputStream
	 *            文件流
	 * @param format
	 *            文件格式，pdf，png等，不包含后缀符号"."
	 * @return
	 */
	public String saveFile(InputStream inputStream, String format, String uid) {
		try {
			GridFS gridFS = getInstance();

			// 随机生成文件名称，多次重试
			String filename = this.randomFileName();
			// 如果有文件重复，则重新生成filename
			while (true) {
				GridFSDBFile _current = gridFS.findOne(filename);
				// 如果文件不存在，则保存操作
				if (_current == null) {
					break;
				}
				filename = this.randomFileName();
			}

			GridFSInputFile file = gridFS.createFile(inputStream, filename);
			if (format != null) {
				file.put("format", format);
			}
			if (uid != null) {
				file.put("uid", uid);
			}
			file.put("content-type", "application/octet-stream");
			file.save();
			return concat(filename, format);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				inputStream.close();
			} catch (Exception ex) {
			}
		}
	}

	private String concat(String filename, String format) {
		if (format == null) {
			return filename;
		}
		if (format.startsWith(".")) {
			return filename + format;
		}
		return filename + "." + format;
	}

	private String randomFileName() {
		return RandomStringUtils.random(32, true, true).toLowerCase();
	}

	public void delete(String filename) {
		GridFS gridFS = getInstance();
		gridFS.remove(filename);
	}

	public InputStream getFile(String filename) {
		GridFS gridFS = getInstance();
		GridFSDBFile _current = gridFS.findOne(filename);
		if (_current == null) {
			return null;
		}
		return _current.getInputStream();
	}

	public InputStream getImage(String filename, String path) throws Exception {
		// 获取最大边,等比缩放
		if (ImageSizeEnum.valueOfPath(path) == null) {
			return null;
		}

		GridFS gridFS = getInstance();
		GridFSDBFile _current = gridFS.findOne(filename);
		if (_current == null) {
			return null;
		}

		int size = ImageSizeEnum.valueOfPath(path).size;

		int max = (Integer) _current.get("max");// 图片的实际尺寸

		InputStream result = null;
		// 裁剪
		if (size < max) {
			InputStream inputStream = _current.getInputStream();
			BufferedImage image = ImageIO.read(inputStream);

			inputStream.close();
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			BufferedImage thumbnail = Scalr.resize(image, size);// 保留最大尺寸
			String format = (String) _current.get("format");
			ImageIO.write(thumbnail, format, bos);
			result = new ByteArrayInputStream(bos.toByteArray());
		} else {
			result = _current.getInputStream();
		}

		return result;
	}

	/**
	 *
	 * @param inputStream
	 *            输入流
	 * @return
	 * @throws Exception
	 */
	public String saveImage(InputStream inputStream, String uid) throws Exception {
		BundleEntry bundleEntry = this.drain(inputStream);
		if (bundleEntry == null) {
			throw new RuntimeException("file isn't a image!");
		}

		ByteArrayInputStream bis = bundleEntry.inputStream;

		String _currentFileName = this.isExistedImage(bundleEntry);

		// 如果文件md5已存在
		if (_currentFileName != null) {
			return _currentFileName;
		}

		String format = bundleEntry.format;
		GridFS gridFS = getInstance();
		String filename = this.randomFileName();
		// 检测文件名称
		while (true) {
			GridFSDBFile _current = gridFS.findOne(filename);
			// 如果文件不存在，则保存操作
			if (_current == null) {
				break;
			}
			// 否则，重新生成文件名称
			filename = randomFileName();
		}
		// 图片处理
		bis.reset();

		// 保存原图
		GridFSInputFile _inputFile = gridFS.createFile(bis, filename);
		if (uid != null) {
			_inputFile.put("uid", uid);
		}
		_inputFile.put("max", bundleEntry.max);
		_inputFile.put("crc", bundleEntry.crc);
		_inputFile.put("format", format);
		_inputFile.put("md5_source", bundleEntry.md5);
		_inputFile.save();

		return concat(filename, format);

	}

	private String isExistedImage(BundleEntry entry) {
		GridFS gridFS = getInstance();
		DBObject query = new BasicDBObject();
		query.put("crc", entry.crc);
		query.put("md5_source", entry.md5);
		GridFSDBFile _current = gridFS.findOne(query);
		// 根据MD5值查询，检测是否存在
		if (_current == null) {
			return null;
		}
		String format = (String) _current.get("format");
		if (format.startsWith(".")) {
			return _current.getFilename() + format;
		}
		return _current.getFilename() + "." + format;
	}

	/**
	 * 因为图片的stream需要reset，所以需要将流全部汲取
	 * 
	 * @param inputStream
	 * @return
	 * @throws Exception
	 */
	protected BundleEntry drain(InputStream inputStream) throws Exception {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		// 计算源文件的md5、crc，以防止图片的重复上传
		Adler32 crc = new Adler32();
		try {
			while (true) {
				int _c = inputStream.read();
				if (_c == -1) {
					break;
				}
				bos.write(_c);
				crc.update(_c);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			inputStream.close();
		}

		// 第一步：图片格式
		List<String> formats = new ArrayList<String>();
		ImageInputStream imageInputStream = ImageIO.createImageInputStream(new ByteArrayInputStream(bos.toByteArray()));
		imageInputStream.mark();
		try {
			Iterator<ImageReader> it = ImageIO.getImageReaders(imageInputStream);
			while (it.hasNext()) {
				ImageReader reader = it.next();
				String format = reader.getFormatName().toLowerCase();
				if (ArrayUtils.contains(IMAGE_FORMAT, format)) {
					formats.add(format);
				}
			}
		} catch (Exception ex) {
		}

		// 如果格式不合法，则直接返回
		if (formats.isEmpty()) {
			try {
				imageInputStream.close();
			} catch (Exception e) {
				//
			}
			return null;
		}

		String md5 = DigestUtils.md5Hex(bos.toByteArray());// 求原始图片的MD5，和crc
		
		imageInputStream.reset();

		BufferedImage image = ImageIO.read(imageInputStream);

		// 获取最大边,等比缩放
		int max = Math.max(image.getHeight(), image.getWidth());

		bos = new ByteArrayOutputStream();
		// 如果尺寸超过最大值，则resize
		if (max > ImageSizeEnum.PIXELS_MAX.size) {
			max = ImageSizeEnum.PIXELS_MAX.size;
		}
		String format = formats.get(0);
		BufferedImage thumbnail = Scalr.resize(image, max);// 保留最大尺寸
		ImageIO.write(thumbnail, format, bos);

		return new BundleEntry(new ByteArrayInputStream(bos.toByteArray()), md5, crc.getValue(), format, max);
	}

	protected class BundleEntry {
		String md5;
		long crc;
		String format;
		int max;
		ByteArrayInputStream inputStream;

		BundleEntry(ByteArrayInputStream inputStream, String md5, long crc, String format, int max) {
			this.md5 = md5;
			this.crc = crc;
			this.inputStream = inputStream;
			this.format = format;
			this.max = max;
		}
	}

	

}
