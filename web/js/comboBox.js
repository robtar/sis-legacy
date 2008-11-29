/************************************************************************************************************
(C) www.dhtmlgoodies.com, September 2005

This is a script from www.dhtmlgoodies.com. You will find this and a lot of other scripts at our website.

Terms of use:
You are free to use this script as long as the copyright message is kept intact. However, you may not
redistribute, sell or repost it without our permission.

Thank you!

www.dhtmlgoodies.com
Alf Magne Kalleland

************************************************************************************************************/

// Path to arrow images
var arrowImage = '../img/comboBox/select_arrow.gif';	// Regular arrow
var arrowImageOver = '../img/comboBox/select_arrow_over.gif';	// Mouse over
var arrowImageDown = '../img/comboBox/select_arrow_down.gif';	// Mouse down


var comboBoxIds = 0;
var currentlyOpenedOptionBox = false;
var editableSelect_activeArrow = false;



function comboBox_switchImageUrl()
{
	if(this.src.indexOf(arrowImage)>=0){
		this.src = this.src.replace(arrowImage,arrowImageOver);
	}else{
		this.src = this.src.replace(arrowImageOver,arrowImage);
	}


}

function comboBox_showOptions()
{
	if(editableSelect_activeArrow && editableSelect_activeArrow!=this){
		editableSelect_activeArrow.src = arrowImage;

	}
	editableSelect_activeArrow = this;
	var optionDiv = document.getElementById('comboBoxOptions' + this.id.replace(/[^\d]/g,''));
	if(optionDiv.style.display=='block'){
		optionDiv.style.display='none';
		this.src = arrowImageOver;
	}else{
		optionDiv.style.display='block';
		this.src = arrowImageDown;
		if(currentlyOpenedOptionBox && currentlyOpenedOptionBox!=optionDiv)currentlyOpenedOptionBox.style.display='none';
		currentlyOpenedOptionBox= optionDiv;
	}
}

function selectOptionValue()
{
	var parentNode = this.parentNode.parentNode;
	var textInput = parentNode.getElementsByTagName('INPUT')[0];
	textInput.value = this.innerHTML;
	this.parentNode.style.display='none';
	document.getElementById('arrowcomboBox' + parentNode.id.replace(/[^\d]/g,'')).src = arrowImageOver;
}
var activeOption;
function highlightcomboBoxOption()
{
	if(this.style.backgroundColor=='#316AC5'){
		this.style.backgroundColor='';
		this.style.color='';
	}else{
		this.style.backgroundColor='#316AC5';
		this.style.color='#FFF';
	}

	if(activeOption){
		activeOption.style.backgroundColor='';
		activeOption.style.color='';
	}
	activeOption = this;

}

function createEditableSelect(dest)
{

	dest.className='comboBoxInput';
	var div = document.createElement('DIV');
	div.style.styleFloat = 'left';
	div.style.width = dest.offsetWidth + 16 + 'px';
	div.style.position = 'relative';
	div.id = 'comboBox' + comboBoxIds;
	var parent = dest.parentNode;
	parent.insertBefore(div,dest);
	div.appendChild(dest);
	div.className='comboBox';
	div.style.zIndex = 10000 - comboBoxIds;

	var img = document.createElement('IMG');
	img.src = arrowImage;
	img.className = 'comboBoxArrow';

	img.onmouseover = comboBox_switchImageUrl;
	img.onmouseout = comboBox_switchImageUrl;
	img.onclick = comboBox_showOptions;
	img.id = 'arrowcomboBox' + comboBoxIds;

	div.appendChild(img);

	var optionDiv = document.createElement('DIV');
	optionDiv.id = 'comboBoxOptions' + comboBoxIds;
	optionDiv.className='comboBoxOptionContainer';
	optionDiv.style.width = div.offsetWidth-2 + 'px';
	div.appendChild(optionDiv);

	if(dest.getAttribute('comboBoxOptions')){
		var options = dest.getAttribute('comboBoxOptions').split(';');
		var optionsTotalHeight = 0;
		var optionArray = new Array();
		for(var no=0;no<options.length;no++){
			var anOption = document.createElement('DIV');
			anOption.innerHTML = options[no];
			anOption.className='comboBoxAnOption';
			anOption.onclick = selectOptionValue;
			anOption.style.width = optionDiv.style.width.replace('px','') - 2 + 'px';
			anOption.onmouseover = highlightcomboBoxOption;
			optionDiv.appendChild(anOption);
			optionsTotalHeight = optionsTotalHeight + anOption.offsetHeight;
			optionArray.push(anOption);
		}
		if(optionsTotalHeight > optionDiv.offsetHeight){
			for(var no=0;no<optionArray.length;no++){
				optionArray[no].style.width = optionDiv.style.width.replace('px','') - 22 + 'px';
			}
		}
		optionDiv.style.display='none';
		optionDiv.style.visibility='visible';
	}

	comboBoxIds = comboBoxIds + 1;
}