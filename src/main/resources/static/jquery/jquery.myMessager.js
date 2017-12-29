/**
 * This messager jQuery plugin 
 *
 * @author cyj
 * @version 1.2
 * @return {Object} jQuery Object
 */
 
(function($){
	function showOverlay(opts) {
		if(opts.isShow){
			$("#"+opts.elId).remove();
				$("body").append('<div id="'+opts.elId+'"></div>');
				$("#"+opts.elId).css({
					position: 'absolute',
					zIndex: opts.zIndex,
					top: '0px',
					left: '0px',
					width: '100%',
					height: $(document).height(),
					background: opts.background,
					opacity: opts.opacity
			});
		}else{
			$("#"+opts.elId).remove();
		}
	};
	$.myOverlay=function(opts){
		var topts=$.extend({},$.myOverlay.defaults,opts);
		showOverlay(topts);
	};
	$.myOverlay.defaults={'elId':'popup_overlay','msg':'','background':'#CCCCCC','opacity':0.2,'zIndex':8999};
})(jQuery);

(function($){
	$.getViewCenterYPosition=function(px){
		var sTop=0;
		var ppx=160;
		if(typeof(px)!="undefined" && !isNaN(px)){
			ppx=px;
		}
		try {
			if(self.frameElement && self.frameElement.tagName=="IFRAME"){
				var top = ($(window.parent).height()-ppx-100)/2;  
				var scrollTop = window.parent.document.documentElement.scrollTop;  
				sTop=scrollTop+top;
			}else{
				var top = ($(window).height()-ppx)/2;  
				var mainScrollTop=$(document).scrollTop(); 
				sTop=mainScrollTop+top;  
			}
		} catch (e) {
			var top = ($(window).height()-ppx)/2;  
			var mainScrollTop=$(document).scrollTop(); 
			sTop=mainScrollTop+top;  
		}
		
		if(sTop<5){
			sTop=5;
		}
		return parseInt(sTop);
	};
})(jQuery);

(function($){
	function creatShadow(msg){
		 var top=$.getViewCenterYPosition();
		 $("<div id=\"myShadow\" class=\"window-mask\"></div>").css({display:"block",width:"100%",height:document.body.scrollHeight}).css('z-index','9998').appendTo("body");
		 $("<div id=\"myShadowMsg\" class=\"window-mask-msg\"></div>").html(msg).appendTo("body").css({display:"block",left:($(document.body).outerWidth(true) - 190) / 2,top:top}).css('z-index','9999'); 
	};
	function removeShadow(){
		 $("#myShadow,#myShadowMsg").remove();
	};
	$.myShadow={
		show:function(msg){
			var msgs='';
			if(!msg || msg==''){
				msgs="正在处理，请稍候....";
			}else{
				msgs=msg;
			}
			creatShadow(msgs);
		},
		remove:function(){
			removeShadow();
		}
	};
	function creatTableShadow(jq){
		 $("<div id=\"myTableShadow\" class=\"window-mask\"></div>").css({display:"block",top:$(jq).offset().top,left:$(jq).offset().left,width:$(jq).outerWidth(true),height:$(jq).outerHeight()}).css('z-index','9998').appendTo($(jq));
		 $("<div id=\"myTableShadowMsg\" class=\"window-mask-msg\"></div>").html("数据加载中，请稍候....").appendTo($(jq)).css({display:"block",left:($(jq).offset().left+($(jq).outerWidth(true) - 100) / 2),top:($(jq).offset().top+($(jq).outerHeight()-16)/2)}).css('z-index','9999'); 
	};
	function removeTableShadow(){
		 $("#myTableShadow,#myTableShadowMsg").remove();
	};
	$.fn.myTableShadow=function(par){
		if(typeof par=="string"){
			return $.fn.myTableShadow.methods[par](this);
		}
	};
	$.fn.myTableShadow.methods={show:function(jq){
		return jq.each(function(){
			creatTableShadow(this);
		});
	},remove:function(jq){ 
		return jq.each(function(){
			removeTableShadow(this);
		});
	}
	};
})(jQuery);

(function($){	
	function rander(type,opts,btns,tools){
		$.myMessager.defaults.zIndex+=2;
		$.myOverlay({'elId':'overlay_'+$.myMessager.defaults.zIndex,'zIndex':$.myMessager.defaults.zIndex,'isShow':$.myMessager.defaults.modal});	
		$container=$("<div class=\"noteBoxFir noteBoxbg\"></div>").css({'zIndex':$.myMessager.defaults.zIndex+1,top:$.getViewCenterYPosition(80)+'px'}).appendTo('body');
		var $incontainer=$("<div class=\"noteBox\"></div>").appendTo($container);	
		var $titleText=$("<div class=\"textTile\"></div>");	
		var $titleRight=$("<a href=\"javascript:void(0);\" class=\"close_tcg\"></a>").bind("click",eval(tools['toolsClose']));;
		$titleText.append("<h3>"+opts.title+"</h3>");
		$titleText.append($titleRight);
		$incontainer.append($titleText);
		$incontainer.append("<div class='textCon'>"+opts.msg+"</div>");
		var $bottom=$("<div class=\"noteBox-bottom\"></div>").appendTo($incontainer);
		var btnNum=0;
		for(var btn in btns){
			var $btnEL=$("<a href=\"javascript:void(0);\" class=\"okBtn\"><s></s>"+btn+"</a>");
			$btnEL.bind("click",eval(btns[btn])).appendTo($bottom);
			$btnEL.wrap("<span class=\"message_btn\"></span>");
			if(type=='alert' && btn=="确定"){
				$btnEL.focus().keydown( function(e) {
					if(e.keyCode == 13 ){  $(this).trigger('click'); return false;};
					if (e.stopPropagation) {
						e.stopPropagation();
					}else {
						e.cancelBubble = true;
					}
				});
			}else if(type=='confirm' && btn=="取消"){
				$btnEL.attr('class','cancelBtn').focus().keydown( function(e) {
					if(e.keyCode == 13 ){  $(this).trigger('click'); return false;};
					if (e.stopPropagation) {
						e.stopPropagation();
					}else {
						e.cancelBubble = true;
					}
				});
			}
			btnNum++;
		}
		//var top = (($(window).height() / 2) - ($container.outerHeight() / 2));
		var top = $.getViewCenterYPosition($container.outerHeight()+40);
		var left = (($(window).width() / 2) - ($container.outerWidth() / 2));
		if( top < 0 ) top = 0;
		if( left < 0 ) left = 0;
		// IE6 fix
//		if( $.browser.msie && parseInt($.browser.version) <= 6 ) top = top + $(window).scrollTop();
		$container.css({
			top: top + 'px',
			left: left + 'px'
		});
		if(opts.boxstyle && opts.boxstyle!=''){
			$container.css(eval('('+opts.boxstyle+')'));
		}
		
		$.data($container,"messager",{'zIndex':$.myMessager.defaults.zIndex});
		
		return $container;
	};
	function messagerClose(jq){
		var zIndex=$.data(jq,"messager").zIndex;
		$.myOverlay({'elId':'overlay_'+zIndex,'isShow':false});
		jq.remove();
	};
	$.myMessager={
		alert : function(opts,fn){
			var topts;
			topts=$.extend({},$.myMessager.defaults,opts);
			var tools={};
			var btns={};
			tools[$.myMessager.defaults.toolsClose]=function(){
				messagerClose(win);
			};
			btns[topts.ok]=function(){
				messagerClose(win);
				if(fn){
					fn();
					return false;
				}
			};
			var win=rander('alert',topts,btns,tools);
			return win;
		},
		confirm : function(opts,fn){
			var topts;
			topts=$.extend({},$.myMessager.defaults,opts);
			var tools={};
			var btns={};
			tools[$.myMessager.defaults.toolsClose]=function(){
				messagerClose(win);
			};
			btns[topts.ok]=function(){
				messagerClose(win);
				if(fn){
					fn(true);
					return false;
				}
				messagerClose(win);
			},
			btns[topts.cancel]=function(){
				messagerClose(win);
				if(fn){
					fn(false);
					return false;
				}
			};
			var win=rander('confirm',topts,btns,tools);
			return win;
		}
	};	
	$.myMessager.defaults={'title':'','msg':'','boxstyle':'','ok':'确定','cancel':'取消','toolsClose':'toolsClose','modal':true,'zIndex':9000};
})(jQuery);

(function($){
	function windowRander(jq,opts){
		var containerHeight=$(jq).outerHeight(true)+400;
		$(jq).attr('jqHeight',containerHeight);
		$(jq).css({'position':'absolute'}).hide();
		var _3=$.data(jq,"myWindow");
		var opts=_3.options;
		$container=$("<div class=\"noteBoxFir noteBoxbg\"></div>");
		var $incontainer=$("<div class=\"noteBox\"></div>");	
		var $titleText=$("<div class=\"textTile\" id=\"title\"></div>");	
		var $titleRight=$("<a href=\"javascript:void(0);\" class=\"close_tcg\"></a>").bind("click",function(){myWindowClose(jq);});
		$titleText.append("<h3>"+opts.title+"</h3>");
		$titleText.append($titleRight);
		$incontainer.append($titleText);
		var contend=$(jq).children().clone();
		$incontainer.append(contend);
		$container.append($incontainer);
		//var top = (($(window).height() / 2) - ($container.outerHeight() / 2));
		$(jq).empty().append($container);
		if(opts.width && opts.width!=''){
			$container.css('width',opts.width);
		}
		var top = $.getViewCenterYPosition(containerHeight);
		top=top>($(document).height()-containerHeight)?($(document).height()-containerHeight):top;
		var left = (($(window).width() / 2) - (opts.width / 2));
		if( top < 0 ) top = 0;
		if( left < 0 ) left = 0;
		// IE6 fix
		if( $.browser.msie && parseInt($.browser.version) <= 6 ) top = top + $(window).scrollTop();
		$(jq).css({
			top: top + 'px',
			left: left + 'px'
		});
		if(opts.top && opts.top!=''){
			$(jq).css('top',opts.top+'px');
		}
		if(opts.left && opts.left!=''){
			$(jq).css('left',opts.left+'px');
		}
		if(opts.boxstyle && opts.boxstyle!=''){
			$container.css(eval('('+opts.boxstyle+')'));
		}
		if(opts.draggable){
			$(jq).myDraggable({handle:'#title'});
		}
		return $container;
	};
	function myWindowClose(jq){
		var _3=$.data(jq,"myWindow");
		var opts=_3.options;
		$.myOverlay({'elId':'winoverlay_'+opts.zIndex,'zIndex':$.myMessager.defaults.zIndex,'isShow':false});
		$(jq).hide();
	};
	function myWindowOpen(jq){
		var _3=$.data(jq,"myWindow");
		var opts=_3.options;
		if($('#winoverlay_'+opts.zIndex)){
			$.myOverlay({'elId':'winoverlay_'+opts.zIndex,'zIndex':opts.zIndex,'isShow':false});
		}
		$.myMessager.defaults.zIndex=$.myMessager.defaults.zIndex+2;
		opts.zIndex=$.myMessager.defaults.zIndex;
		$.myOverlay({'elId':'winoverlay_'+opts.zIndex,'zIndex':opts.zIndex,'isShow':opts.modal});
//		var top=$(jq).css("top");
//		var containerHeight=$(jq).attr('jqHeight');
//		top=top>($(document).height()-containerHeight)?($(document).height()-containerHeight):top;
		$(jq).css('zIndex',(opts.zIndex+1)).show();
		
	};
	function moveWindow(jq,parm){
		if(typeof parm=='string'){
			$(jq).css(eval('('+parm+')'));
		}else{
			$(jq).css(parm);
		}
	}
	$.fn.myWindow=function(par,parm){
		if(typeof par=="string"){
			return $.fn.myWindow.methods[par](this,parm);
		}
		par=par||{};
		return this.each(function(){
			var _1c;
			_1c=$.extend({},$.fn.myWindow.defaults,par);
			$.data(this,"myWindow",{options:_1c});
			windowRander(this);
		});
	};
	$.fn.myWindow.methods={options:function(jq){
		return $.data(jq[0],"myWindow").options;
	},open:function(jq){
		return jq.each(function(){
			myWindowOpen(this);
		});
	},close:function(jq,page_id){ 
		return jq.each(function(){
			myWindowClose(this);
		});
	},moveTo:function(jq,parm){ 
		return jq.each(function(){
			moveWindow(this,parm);
		});
	}
	};
	$.fn.myWindow.defaults={'title':'','boxstyle':'','modal':true,'top':'100','left':'','width':'339','height':'','zIndex':'9000',draggable:true};
})(jQuery);


(function($){
	function wrapTabs(container) {
		var opts = $.data(container, 'tabs').options;
		var tabs = $.data(container, 'tabs').tabs;
		var cc = $(container);
		cc.css({'width':opts.width,'height':opts.height});
		var tabsul=$('<ul class="mytabs tabsclearfix"></ul>').prependTo(container);
		cc.children('div').each(function(i){
			var title=$(this).attr('title');
			var status=$(this).attr('selected');
			var tab=$('<li tab-index="'+i+'">'+title+'</li>').appendTo(tabsul);
			if(status){
				tab.addClass('curSelect');
			}
			tab.bind('click',function(){
				selectTab(container, getTabIndex(container, this));
			});
			tabs.push(tab);
			$(this).remove();
		});
		var tabsContent=$("#"+opts.tabsContentId);
		if(tabsContent){
			tabsContent.addClass('tabsContent').children().hide();
			tabsContent.appendTo(container);
			$.data(container, 'tabs').tabsContent=tabsContent;
		}
	}
	function showContent(container,index){
		var tabsContent = $.data(container, 'tabs').tabsContent;
		if(tabsContent){
			tabsContent.find("div:eq("+index+")").show();
		}
	}
	function hideContent(container,index){
		var tabsContent = $.data(container, 'tabs').tabsContent;
		if(tabsContent){
			tabsContent.find("div:eq("+index+")").hide();
		}
	}
	function selectTab(container,index){
		var opts = $.data(container, 'tabs').options;
		var tabs = $.data(container, 'tabs').tabs;
		if (tabs.length == 0) return;
		var selected = getSelectedTab(container);
		if (selected){
			selected.removeClass('curSelect');
			$.data(container, 'tabs').preSelectTab=selected;
			var preSelectedIndex=getTabIndex(container,selected);
			hideContent(container,preSelectedIndex);
		}
		var tab=getTab(container,index);
		tab.addClass('curSelect');
		var title=tab.html();
		showContent(container,index);
		opts.onSelect.call(container, title, index);
	}
	
	function getTabIndex(container, tab){
		var tabs = $.data(container, 'tabs').tabs;
		for(var i=0; i<tabs.length; i++){
			if (tabs[i][0] == $(tab)[0]){
				return i;
			}
		}
		return -1;
	}

	function updateTab(container, param){
	//	var selectHis = $.data(container, 'tabs').selectHis;
		var pp = param.tab;	// the tab panel
//		var oldTitle = pp.html(); 
		pp.html(param.options.title);
		
	}
	
	/**
	 * get the specified tab panel
	 */
	function getTab(container, which, removeit){
		var tabs = $.data(container, 'tabs').tabs;
		if (typeof which == 'number'){
			if (which < 0 || which >= tabs.length){
				return null;
			} else {
				var tab = tabs[which];
				if (removeit) {
					tabs.splice(which, 1);
				}
				return tab;
			}
		}
		for(var i=0; i<tabs.length; i++){
			var tab = tabs[i];
			if (tab.html() == which){
				if (removeit){
					tabs.splice(i, 1);
				}
				return tab;
			}
		}
		return null;
	}
	
	function getSelectedTab(container){
		var tabs = $.data(container, 'tabs').tabs;
		for(var i=0; i<tabs.length; i++){
			var tab = tabs[i];
			if (tab.hasClass('curSelect')){
				return tab;
			}
		}
		return null;
	}
	function getPreSelectedTab(container){
		return $.data(container, 'tabs').preSelectTab;
	}

	
	function exists(container, which){
		return getTab(container, which) != null;
	}
	
	function getTabTitle(container, tab){
		var tabs = $.data(container, 'tabs').tabs;
		for(var i=0; i<tabs.length; i++){
			if (tabs[i][0] == tab[0]){
				var title=tab.html();
				return title;
			}
		}
		return "";
		
	}
	/**
	 * do first select action, if no tab is setted the first tab will be selected.
	 */
	function doFirstSelect(container){
		var tabs = $.data(container, 'tabs').tabs;
		for(var i=0; i<tabs.length; i++){
			if (tabs[i].hasClass('curSelect')){
				selectTab(container, i);
				return;
			}
		}
		if (tabs.length){
			selectTab(container, 0);
		}
	}
	
	$.fn.myTabs = function(options, param){
		if (typeof options == 'string') {
			return $.fn.myTabs.methods[options](this, param);
		}
		options = options || {};
		return this.each(function(){
			var state = $.data(this, 'tabs');
			var opts;
			if (state) {
				opts = $.extend(state.options, options);
				state.options = opts;
				
			} else {
				$.data(this, 'tabs', {
					options: $.extend({},$.fn.myTabs.defaults, options),
					tabs: [],
					tabsContent:null,
					preSelectTab:null
				});
				wrapTabs(this);
			}
		
			doFirstSelect(this);
		});
	};
	
	$.fn.myTabs.methods = {
		options: function(jq){
			return $.data(jq[0], 'tabs').options;
		},
		tabs: function(jq){
			return $.data(jq[0], 'tabs').tabs;
		},
		getTab: function(jq, which){
			return getTab(jq[0], which);
		},
		getTabIndex: function(jq, tab){
			return getTabIndex(jq[0], tab);
		},
		getSelected: function(jq){
			return getSelectedTab(jq[0]);
		},
		exists: function(jq, which){
			return exists(jq[0], which);
		},
		update: function(jq, options){
			updateTab(jq[0], options);
		},
		getTabTitle: function(jq, tab){
			return getTabTitle(jq[0], tab);
		},
		getPreSelectedTab : function(jq){
			return getPreSelectedTab(jq[0]);
		}
	};

	$.fn.myTabs.defaults = {
		width: 'auto',
		height: 'auto',
		tabsContentId:'tabsContent',
		onSelect: function(title, index){}
	};
})(jQuery);

/**
 * draggable - jQuery EasyUI
 */
(function($){
	var isDragging = false;
	function drag(e){
		var state = $.data(e.data.target, 'draggable');
		var opts = state.options;
		var proxy = state.proxy;
		
		var dragData = e.data;
		var left = dragData.startLeft + e.pageX - dragData.startX;
		var top = dragData.startTop + e.pageY - dragData.startY;
		
		if (proxy){
			if (proxy.parent()[0] == document.body){
				if (opts.deltaX != null && opts.deltaX != undefined){
					left = e.pageX + opts.deltaX;
				} else {
					left = e.pageX - e.data.offsetWidth;
				}
				if (opts.deltaY != null && opts.deltaY != undefined){
					top = e.pageY + opts.deltaY;
				} else {
					top = e.pageY - e.data.offsetHeight;
				}
			} else {
				if (opts.deltaX != null && opts.deltaX != undefined){
					left += e.data.offsetWidth + opts.deltaX;
				}
				if (opts.deltaY != null && opts.deltaY != undefined){
					top += e.data.offsetHeight + opts.deltaY;
				}
			}
		}
		
//		if (opts.deltaX != null && opts.deltaX != undefined){
//			left = e.pageX + opts.deltaX;
//		}
//		if (opts.deltaY != null && opts.deltaY != undefined){
//			top = e.pageY + opts.deltaY;
//		}
		
		if (e.data.parent != document.body) {
			left += $(e.data.parent).scrollLeft();
			top += $(e.data.parent).scrollTop();
		}
		
		if (opts.axis == 'h') {
			dragData.left = left;
		} else if (opts.axis == 'v') {
			dragData.top = top;
		} else {
			dragData.left = left;
			dragData.top = top;
		}
	}
	
	function applyDrag(e){
		var state = $.data(e.data.target, 'draggable');
		var opts = state.options;
		var proxy = state.proxy;
		if (!proxy){
			proxy = $(e.data.target);
		}
//		if (proxy){
//			proxy.css('cursor', opts.cursor);
//		} else {
//			proxy = $(e.data.target);
//			$.data(e.data.target, 'draggable').handle.css('cursor', opts.cursor);
//		}
		proxy.css({
			left:e.data.left,
			top:e.data.top
		});
		$('body').css('cursor', opts.cursor);
	}
	
	function doDown(e){
		isDragging = true;
		var state = $.data(e.data.target, 'draggable');
		var opts = state.options;
		
		var droppables = $('.droppable').filter(function(){
			return e.data.target != this;
		}).filter(function(){
			var accept = $.data(this, 'droppable').options.accept;
			if (accept){
				return $(accept).filter(function(){
					return this == e.data.target;
				}).length > 0;
			} else {
				return true;
			}
		});
		state.droppables = droppables;
		
		var proxy = state.proxy;
		if (!proxy){
			if (opts.proxy){
				if (opts.proxy == 'clone'){
					proxy = $(e.data.target).clone().insertAfter(e.data.target);
				} else {
					proxy = opts.proxy.call(e.data.target, e.data.target);
				}
				state.proxy = proxy;
			} else {
				proxy = $(e.data.target);
			}
		}
		
		proxy.css('position', 'absolute');
		drag(e);
		applyDrag(e);
		
		opts.onStartDrag.call(e.data.target, e);
		return false;
	}
	
	function doMove(e){
		var state = $.data(e.data.target, 'draggable');
		drag(e);
		if (state.options.onDrag.call(e.data.target, e) != false){
			applyDrag(e);
		}
		
		var source = e.data.target;
		state.droppables.each(function(){
			var dropObj = $(this);
			if (dropObj.droppable('options').disabled){return;}
			
			var p2 = dropObj.offset();
			if (e.pageX > p2.left && e.pageX < p2.left + dropObj.outerWidth()
					&& e.pageY > p2.top && e.pageY < p2.top + dropObj.outerHeight()){
				if (!this.entered){
					$(this).trigger('_dragenter', [source]);
					this.entered = true;
				}
				$(this).trigger('_dragover', [source]);
			} else {
				if (this.entered){
					$(this).trigger('_dragleave', [source]);
					this.entered = false;
				}
			}
		});
		
		return false;
	}
	
	function doUp(e){
		isDragging = false;
//		drag(e);
		doMove(e);
		
		var state = $.data(e.data.target, 'draggable');
		var proxy = state.proxy;
		var opts = state.options;
		if (opts.revert){
			if (checkDrop() == true){
				$(e.data.target).css({
					position:e.data.startPosition,
					left:e.data.startLeft,
					top:e.data.startTop
				});
			} else {
				if (proxy){
					var left, top;
					if (proxy.parent()[0] == document.body){
						left = e.data.startX - e.data.offsetWidth;
						top = e.data.startY - e.data.offsetHeight;
					} else {
						left = e.data.startLeft;
						top = e.data.startTop;
					}
					proxy.animate({
						left: left,
						top: top
					}, function(){
						removeProxy();
					});
				} else {
					$(e.data.target).animate({
						left:e.data.startLeft,
						top:e.data.startTop
					}, function(){
						$(e.data.target).css('position', e.data.startPosition);
					});
				}
			}
		} else {
			$(e.data.target).css({
				position:'absolute',
				left:e.data.left,
				top:e.data.top
			});
			checkDrop();
		}
		
		opts.onStopDrag.call(e.data.target, e);
		
		$(document).unbind('.draggable');
		setTimeout(function(){
			$('body').css('cursor','');
		},100);
		
		function removeProxy(){
			if (proxy){
				proxy.remove();
			}
			state.proxy = null;
		}
		
		function checkDrop(){
			var dropped = false;
			state.droppables.each(function(){
				var dropObj = $(this);
				if (dropObj.droppable('options').disabled){return;}
				
				var p2 = dropObj.offset();
				if (e.pageX > p2.left && e.pageX < p2.left + dropObj.outerWidth()
						&& e.pageY > p2.top && e.pageY < p2.top + dropObj.outerHeight()){
					if (opts.revert){
						$(e.data.target).css({
							position:e.data.startPosition,
							left:e.data.startLeft,
							top:e.data.startTop
						});
					}
					removeProxy();
					$(this).trigger('_drop', [e.data.target]);
					dropped = true;
					this.entered = false;
					return false;
				}
			});
			if (!dropped && !opts.revert){
				removeProxy();
			}
			return dropped;
		}
		
		return false;
	}
	
	$.fn.myDraggable = function(options, param){
		if (typeof options == 'string'){
			return $.fn.myDraggable.methods[options](this, param);
		}
		
		return this.each(function(){
			var opts;
			var state = $.data(this, 'draggable');
			if (state) {
				state.handle.unbind('.draggable');
				opts = $.extend(state.options, options);
			} else {
				opts = $.extend({}, $.fn.myDraggable.defaults, options || {});
			}
			
			if (opts.disabled == true) {
				$(this).css('cursor', '');
//				$(this).css('cursor', 'default');
				return;
			}
			
			var handle = null;
            if (typeof opts.handle == 'undefined' || opts.handle == null){
                handle = $(this);
            } else {
                handle = (typeof opts.handle == 'string' ? $(opts.handle, this) : opts.handle);
            }
			$.data(this, 'draggable', {
				options: opts,
				handle: handle
			});
			
			handle.unbind('.draggable').bind('mousemove.draggable', {target:this}, function(e){
				if (isDragging) return;
				var opts = $.data(e.data.target, 'draggable').options;
				if (checkArea(e)){
					$(this).css('cursor', opts.cursor);
				} else {
					$(this).css('cursor', '');
				}
			}).bind('mouseleave.draggable', {target:this}, function(e){
				$(this).css('cursor', '');
			}).bind('mousedown.draggable', {target:this}, function(e){
				if (checkArea(e) == false) return;
				$(this).css('cursor', '');

				var position = $(e.data.target).position();
				var offset = $(e.data.target).offset();
				var data = {
					startPosition: $(e.data.target).css('position'),
					startLeft: position.left,
					startTop: position.top,
					left: position.left,
					top: position.top,
					startX: e.pageX,
					startY: e.pageY,
					offsetWidth: (e.pageX - offset.left),
					offsetHeight: (e.pageY - offset.top),
					target: e.data.target,
					parent: $(e.data.target).parent()[0]
				};
				
				$.extend(e.data, data);
				var opts = $.data(e.data.target, 'draggable').options;
				if (opts.onBeforeDrag.call(e.data.target, e) == false) return;
				
				$(document).bind('mousedown.draggable', e.data, doDown);
				$(document).bind('mousemove.draggable', e.data, doMove);
				$(document).bind('mouseup.draggable', e.data, doUp);
//				$('body').css('cursor', opts.cursor);
			});
			
			// check if the handle can be dragged
			function checkArea(e) {
				var state = $.data(e.data.target, 'draggable');
				var handle = state.handle;
				var offset = $(handle).offset();
				var width = $(handle).outerWidth();
				var height = $(handle).outerHeight();
				var t = e.pageY - offset.top;
				var r = offset.left + width - e.pageX;
				var b = offset.top + height - e.pageY;
				var l = e.pageX - offset.left;
				
				return Math.min(t,r,b,l) > state.options.edge;
			}
			
		});
	};
	
	$.fn.myDraggable.methods = {
		options: function(jq){
			return $.data(jq[0], 'draggable').options;
		},
		proxy: function(jq){
			return $.data(jq[0], 'draggable').proxy;
		},
		enable: function(jq){
			return jq.each(function(){
				$(this).draggable({disabled:false});
			});
		},
		disable: function(jq){
			return jq.each(function(){
				$(this).draggable({disabled:true});
			});
		}
	};
	
	
	$.fn.myDraggable.defaults = {
		proxy:null,	// 'clone' or a function that will create the proxy object, 	// the function has the source parameter that indicate the source object dragged.
		revert:false,
		cursor:'move',
		deltaX:null,
		deltaY:null,
		handle: null,
		disabled: false,
		edge:0,
		axis:null,	// v or h
		
		onBeforeDrag: function(e){},
		onStartDrag: function(e){},
		onDrag: function(e){},
		onStopDrag: function(e){}
	};
})(jQuery);



(function($){
	function tableRander(jq){
		var opts=$.data(jq,"myTable").options;
		var columns=opts.columns;
		var tCont=[];
		var fp=$('<div class="my_table_wrap" style="width:'+opts.width+';"></div>');
		$(jq).wrap(fp);
		$.data(jq,"tableWraper",{tableWraper:fp});
		$(jq).addClass('my_table').css({'width':opts.width});
		tCont.push('<thead><tr>');
		if(opts.rownumber){
			tCont.push('<th width="40px" class="th_rownumber">行</th>');
		}
		if(opts.checkbox){
			tCont.push('<th width="40px" class="th_checkbox"><input type="checkbox" id="row_checkAll" name="allcheckbox" /></th>');
		}
		for(var i=0;i<columns.length;i++){
			var align="center";
			if(columns[i].align){
				align=columns[i].align;
			}	
			tCont.push('<th class="mytable_th" style="'+columns[i].style+'" width="'+columns[i].width+'" align="'+align+'">');
			tCont.push(columns[i].title);
			tCont.push('</th>');
		}
		tCont.push('</tr></thead>');
		tCont.push('<tbody></tbody>');
		$(jq).html(tCont.join(''));
		if(opts.checkbox){
			$(jq).find('#row_checkAll').click(function(){
				if($(this).attr('checked')){
					$(jq).find("input[name='subcheckbox']").attr("checked",true);
				}else{
					$(jq).find("input[name='subcheckbox']:checkbox:checked").removeAttr('checked');
				}
			});
		}		
		if(opts.pagination){
			var pagination_top=null;
			if(opts.paginationTop){
				pagination_top=$('<div class="pagination_top"></div>');
				pagination_top.insertBefore($(jq));
			}
			var pagination_bot=$('<div style="float:right;margin-top:6px;" class="pagination_bot"> </div>');
			pagination_bot.insertAfter($(jq));
			var pagination=pagination_bot.myPagination({total:0,items_per_page:opts.pageSize,pageList:opts.pageList,topjq:pagination_top,callback:function(pageNumber,pjq){
				var popts= pjq.myPagination('options');
				var pageSize=popts.items_per_page;
				var pparm={pageNumber:pageNumber,pageSize:pageSize};
				opts.queryParams=$.extend({},pparm,opts.queryParams);
				opts.pageNumber=pageNumber;
				opts.pageSize=pageSize;
				reload(jq);
			}});
			$.data(jq,"pagination",{pagination:pagination});
		}
		loadData(jq);
	}
	function fillTable(jq,rowDatas){
		var opts=$.data(jq,"myTable").options;
		var tbody=$(jq).find('tbody').empty();
		if(!rowDatas || rowDatas.length==0){
			var columns=opts.columns;
			var colspan=columns.length;
			if(opts.rownumber){
				colspan+=1;
			}
			if(opts.checkbox){
				colspan+=1;
			}
			tbody.html('<tr><td colspan="'+colspan+'" style="height:80px;" align="center"><span style="color:red;">无数据记录!</span>&nbsp;</td></tr>');
			return;
		}
		var trs=[];
		$.each(rowDatas,function(i,v){
			var tr=$('<tr row-index="'+i+'"></tr>');
			var tds=createRow(jq,i,v);
			for(var i=0;i<tds.length;i++){
				tr.append(tds[i]);
				trs.push(tr);
			}
		});
		for(var i=0;i<trs.length;i++){
			tbody.append(trs[i]);
		}
		if(opts.checkbox){
			$(jq).find("input[name='subcheckbox']").click(function(){
				if(!$(this).attr('checked')){
					$(jq).find('#row_checkAll').removeAttr("checked");
				}else{
					var unchecknum=$(jq).find("input[name='subcheckbox']").not("input:checked").length;
					if(unchecknum==0){
						$(jq).find('#row_checkAll').attr("checked",true);
					}
				}
			});
		}
		if(typeof opts.onTableReady == 'function'){
			opts.onTableReady();
		}
	}
	function createRow(jq,index,rowData){
		var opts=$.data(jq,"myTable").options;
		var columns=opts.columns;
		var tds=[];
		if(opts.rownumber){
			var pageNumber=opts.pageNumber;
			var pageSize=opts.pageSize;
			var rowIndex=(pageNumber-1<0?0:pageNumber-1)*pageSize+(parseInt(index)+1);
			tds.push($('<td class="th_rownumber">'+rowIndex+'</td>'));
		}
		if(opts.checkbox){
			tds.push($('<td class="th_checkbox"><input type="checkbox" name="subcheckbox" value="'+index+'"/></td>'));
		}
		$.each(columns,function(i,v){
			var tdattr=" ";
			if(v.rowspan){
				tdattr+="rowspan=\""+v.rowspan+"\"";
			}
			if(v.colspan){
				tdattr+="colspan=\""+v.colspan+"\"";
			}
			if(v.style){
				tdattr+="style=\""+v.style+"\"";
			}
			var td=$('<td'+tdattr+'></td>');
			if(v.align){
				td.attr('align',v.align);
			}
			var tdVal="";
			if(v.field){
				var field=v.field;
				td.attr('field',field);
				if(v.formatter){
					tdVal=rowData[field];
					tdVal=v.formatter(tdVal,rowData,index);
				}else{
					tdVal=getTdValForField(field,rowData);
				}
			}
			td.html('<span class="myTable_cell">'+tdVal+'</span>');
			tds.push(td);
		});
		return tds;
	}
	
	function getTdValForField(field,rowData){
		var tdVal;
		var pIndex=field.indexOf(".");
		var tIndex=field.indexOf("%");
		var aIndex=field.indexOf("+");
		if(pIndex+tIndex+aIndex==-3){
			tdVal=rowData[field];
		}else{
			var values=[];
			if(aIndex>-1){
				var fields=field.split("+");
				for(var i=0;i<fields.length;i++){
					//var fieldNew=fields[i];
					//alert(fields[i]);
					var value=analyzeField(fields[i],rowData);
					values.push(value);
				}
			}else{
				 var value=analyzeField(field,rowData);
				 values.push(value);
			}
			tdVal=values.join("");
		}
		if(typeof tdVal == 'undefined'){
			tdVal="";
		}
		return tdVal;
	}
	function analyzeField(field,rowData){
		var value="";
		var pIndex=field.indexOf(".");
		var tIndex=field.indexOf("%");
		if(tIndex==0){
			field=field.substring(1,field.length);
		}
		if(pIndex>0){
			var new_fields=field.split(".");
			if(new_fields.length==2){
				var subObj=rowData[new_fields[0]];
				if(typeof subObj != 'undefined'  && subObj!=null){
					value=subObj[new_fields[1]];
				}
			}
		}else{
			value=rowData[field];
			if(typeof value == 'undefined'){
				value=field;
//				value="";
			}
		}
		if(tIndex==0 && typeof value != 'undefined' && value != null && typeof(value.time) != 'undefined'){
			value=dateTimeToLocalString(value.time);
		}
		return value;
	}
	//数据装载
	function loadData(jq){
		var opts=$.data(jq,"myTable").options;
		if($.trim(opts.url).length==0){
			 loadDataSuccess(jq,{'rows':[],'total':0});
			 return;
		}
		var queryParams=opts.queryParams;
		opts.pageNumber=opts.pageNumber<=0?1:opts.pageNumber;
		opts.queryParams=$.extend({},queryParams,{pageNumber:opts.pageNumber,pageSize:opts.pageSize});
		$(jq).find("#row_checkAll").removeAttr("checked");
		$(jq).myTableShadow('show');
		$.ajax({
			  type: "POST",
			  url: opts.url,
			  dataType: "json",
			  data: opts.queryParams,
			  success: function(result){
				 $(jq).myTableShadow('remove');
				 loadDataSuccess(jq,result);
			  },
			  error:function(){
				  $(jq).myTableShadow('remove');
				  loadDataError(jq);
			  }
		});
	}
	//数据装载成功
	function loadDataSuccess(jq,result){
		 var opts=$.data(jq,"myTable").options;
		 var total=result.total;
		 var rows=result.rows;
		 if(typeof rows == 'undefined'  || rows==null){
			 rows=[];
		 }
		 var tableData=$.data(jq,"myTable").tableData;
		 tableData.total=total;
		 tableData.rows=rows;
		 if(!(rows.length==0 && opts.pageNumber!=1)){
			 var pagination=getPager(jq);
			 if(typeof pagination != 'undefined' && pagination != null){
				 var popts=pagination.myPagination('options');
				 popts.total=total;
				 popts.current_page=opts.pageNumber-1<0?0:(opts.pageNumber-1);
				 popts.items_per_page=opts.pageSize;
				 pagination.myPagination('refresh');
			 }
		 }
		 if(typeof opts.onLoadSuccess == 'function'){
			 opts.onLoadSuccess(result,opts);
		 }
		 fillTable(jq,rows);
	}
	//数据装载失败
	function loadDataError(jq){
		var opts=$.data(jq,"myTable").options;
		var tableData=$.data(jq,"myTable").tableData;
		tableData.total=0;
		tableData.rows={};
		var columns=opts.columns;
		var colspan=columns.length;
		if(opts.rownumber){
			colspan+=1;
		}
		if(opts.checkbox){
			colspan+=1;
		}
		$(jq).find('tbody').html('<tr><td colspan="'+colspan+'" style="height:80px;" align="center"><span style="color:red;">查询数据记录异常!</span>&nbsp;</td></tr>');
		var pagination=getPager(jq) ;
		if(typeof pagination != 'undefined' && pagination != null){
			var opts=pagination.myPagination('options');
			opts.total=0;
			pagination.myPagination('refresh');
		}
	}
	//数据装载
	function reload(jq){
		var opts=$.data(jq,"myTable").options;
		var queryParams=opts.queryParams;
		opts.pageNumber=opts.pageNumber<=0?1:opts.pageNumber;
		opts.queryParams=$.extend({},queryParams,{pageNumber:opts.pageNumber,pageSize:opts.pageSize});
		$(jq).find("#row_checkAll").removeAttr("checked");
		$(jq).myTableShadow('show');
		$.ajax({
			  type: "POST",
			  url: opts.url,
			  dataType: "json",
			  data:opts.queryParams,
			  success: function(result){
				 $(jq).myTableShadow('remove');
				 loadDataSuccess(jq,result);
			  },
			  error:function(){
				  $(jq).myTableShadow('remove');
				  loadDataError(jq);
			  }
		});
	}
	function getPager(jq){
		 var opts=$.data(jq,"myTable").options;
		 if(opts.pagination){
			var pagination=$.data(jq,"pagination").pagination;
			return pagination;
		 }else{
			return null;
		 }	 
	}
	function deleteRow(jq,index){
		var opts=$.data(jq,"myTable").options;
		var tableData=$.data(jq,"myTable").tableData;
		var rowDatas=tableData.rows;
		var totalPag=Math.ceil(tableData.total/opts.pageSize);
		if(opts.pageNumber==totalPag && rowDatas.length>1){
			tableData.total-=1;
			rowDatas.splice(index,1);
			refreshTable(jq);
		}else if(rowDatas.length==1){
			if(opts.pageNumber==totalPag){
				opts.pageNumber-=1;
			}
			reload(jq);
		}else{
			reload(jq);
		}
	}
	function refreshRow(jq,index){
		var opts=$.data(jq,"myTable").options;
		var rowDatas= getDatas(jq).rows;
		var tds=createRow(jq,index,rowDatas[index]);
		var tr=$(jq).find("tr[row-index="+index+"]").empty();
		for(var i=0;i<tds.length;i++){
			tr.append(tds[i]);
		}
		if(opts.checkbox){
			$(jq).find('#row_checkAll').removeAttr("checked");
			tr.find("input[name='subcheckbox']").click(function(){
				if(!$(this).attr('checked')){
					$(jq).find('#row_checkAll').removeAttr("checked");
				}else{
					var unchecknum=$(jq).find("input[name='subcheckbox']").not("input:checked").length;
					if(unchecknum==0){
						$(jq).find('#row_checkAll').attr("checked",true);
					}
				}
			});
		}
		if(typeof opts.onTableReady == 'function'){
			opts.onTableReady();
		}
	}
	function refreshTable(jq){
		var topts=$.data(jq,"myTable").options;
		 $(jq).find('#row_checkAll').removeAttr("checked");
		 var rowDatas= getDatas(jq).rows;
		 fillTable(jq,rowDatas);
		 var pagination=getPager(jq) ;
		 if(typeof pagination != 'undefined' && pagination != null){
				var opts=pagination.myPagination('options');
				opts.total=getDatas(jq).total;
				pagination.myPagination('refresh');
		}
		if(typeof topts.onTableReady == 'function'){
			topts.onTableReady();
		}
	}
	function getDatas(jq){
		var tableData=$.data(jq,"myTable").tableData;
		return tableData;
	}
	function getRowData(jq,index){
		var tableData=$.data(jq,"myTable").tableData;
		return tableData.rows[index];
	}
	function getCheckedRows(jq){
		 var rowDatas= getDatas(jq).rows;
		 var checkedRows={};
		$(jq).find("input[name='subcheckbox']:checkbox:checked").each(function(){
			var index=$(this).val();
			checkedRows[index]=rowDatas[index];
		});
		return checkedRows;
	}
	function checkAll(){
		$(jq).find("input[name='subcheckbox']:checkbox").attr("checked",true);
	}
	function uncheckAll(){
		$(jq).find("input[name='subcheckbox']:checkbox").attr("checked",false);
	}
	function dateTimeToLocalString(dateTime){
	if(typeof dateTime=="undefined" || dateTime==null){
		return "";
	}
	var date = new Date(dateTime);
    Y=date.getFullYear(); 
    M=(date.getMonth()+1)<10?("0"+(date.getMonth()+1)) : (date.getMonth()+1);
    D=date.getDate()<10?("0"+date.getDate()) : date.getDate();
    var hh = date.getHours()<10?("0"+date.getHours()) : date.getHours();
    var mm = date.getMinutes()<10?("0"+date.getMinutes()) : date.getMinutes();
	return Y+'-'+M+'-'+D+' '+hh+':'+mm; 
}
	$.fn.myTable=function(par,parm){
		if(typeof par=="string"){
			return $.fn.myTable.methods[par](this,parm);
		}
		par=par||{};
		var options=$.extend({},$.fn.myTable.defaults,par);
		return this.each(function(){
			$.data(this,"myTable",{options:options,tableData:{total:0,rows:{}}});
			tableRander(this);
		});
	};
	$.fn.myTable.methods={
		options:function(jq){
			return $.data(jq[0],"myTable").options;
		},refreshRow:function(jq,index){
			return jq.each(function(){
				refreshRow(this,index);
			});
		},reload:function(jq){
			return jq.each(function(){
				reload(this);
			});
		},getPager:function(jq){
			return getPager(jq[0]);
		},deleteRow:function(jq,index){
			return jq.each(function(){
				deleteRow(this,index);
			});
		},getDatas:function(jq){
			return getDatas(jq[0]);
		},getCheckedRows:function(jq){
			return getCheckedRows(jq[0]);
		},getRowData:function(jq,index){
			return getRowData(jq[0],index);
		},checkAll:function(jq,index){
			return jq.each(function(){
				checkAll(this,index);
			});
		},uncheckAll:function(jq,index){
			return jq.each(function(){
				uncheckAll(this,index);
			});
		},refreshTable:function(jq){
			return jq.each(function(){
				refreshTable(this);
			});
		}
	};
	$.fn.myTable.defaults={checkbox:false,rownumber:false,url:'',columns:{},queryParams:{},width:'100%',height:'',pagination:false,paginationTop:false,pageNumber:1,pageSize:10,pageList:[10,20,30],onLoadSuccess:function(){}};
})(jQuery);

(function($){
	function _f(_10){
//		var _11=$.data(_10,"numberbox").options;
			$(_10).unbind(".numberbox").bind("keypress.numberbox",function(e){
				if(e.which==45){
					if($(this).val().indexOf("-")==-1){
						return true;
					}else{
						return false;
					}
				}
				if(e.which==46){
					if($(this).val().indexOf(".")==-1){
						return true;
					}else{
						return false;
					}
				}else{
					if((e.which>=48&&e.which<=57&&e.ctrlKey==false&&e.shiftKey==false)||e.which==0||e.which==8){
						return true;
					}else{
					if(e.ctrlKey==true&&(e.which==99||e.which==118)){
						return true;
					}else{
						return false;
					}
				}
			}
		});
	}
	$.fn.YHDNumberBox=function(opts){
		return this.each(function(){
			_f(this);
		});
	};
})(jQuery);
