	
	jQuery.fn.serializeObject = function(){  
	   var result = {};  
	   var fieldArray = this.serializeArray();  
	   jQuery.each(fieldArray, function() {  
		   if (result[this.name]) {  
			   if (!result[this.name].push) {  
				   result[this.name] = [result[this.name]];  
			   }  
			   result[this.name].push(this.value || '');  
		   } else {  
			   result[this.name] = this.value || '';  
		   }  
	   });  
	   return result;  
	};


    function fromToJson(form) {
        var result = {};
        var fieldArray = jQuery('#' + form).serializeArray();
        for (var i = 0; i < fieldArray.length; i++) {
            var field = fieldArray[i];
            if (field.name in result) {
                result[field.name] += ',' + field.value;
            } else {
                result[field.name] = field.value;
            }
        }
        return result;
    }