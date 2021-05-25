/*! modernizr 3.6.0 (Custom Build) | MIT *
 * https://modernizr.com/download/?-arrow-contextmenu-cssescape-customprotocolhandler-setclasses !*/
!function(window,document,undefined){function is(e,n){return typeof e===n}function testRunner(){var e,n,o,r,s,t,i;for(var a in tests)if(tests.hasOwnProperty(a)){if(e=[],n=tests[a],n.name&&(e.push(n.name.toLowerCase()),n.options&&n.options.aliases&&n.options.aliases.length))for(o=0;o<n.options.aliases.length;o++)e.push(n.options.aliases[o].toLowerCase());for(r=is(n.fn,"function")?n.fn():n.fn,s=0;s<e.length;s++)t=e[s],i=t.split("."),1===i.length?Modernizr[i[0]]=r:(!Modernizr[i[0]]||Modernizr[i[0]]instanceof Boolean||(Modernizr[i[0]]=new Boolean(Modernizr[i[0]])),Modernizr[i[0]][i[1]]=r),classes.push((r?"":"no-")+i.join("-"))}}function setClasses(e){var n=docElement.className,o=Modernizr._config.classPrefix||"";if(isSVG&&(n=n.baseVal),Modernizr._config.enableJSClass){var r=new RegExp("(^|\\s)"+o+"no-js(\\s|$)");n=n.replace(r,"$1"+o+"js$2")}Modernizr._config.enableClasses&&(n+=" "+o+e.join(" "+o),isSVG?docElement.className.baseVal=n:docElement.className=n)}var classes=[],tests=[],ModernizrProto={_version:"3.6.0",_config:{classPrefix:"",enableClasses:!0,enableJSClass:!0,usePrefixes:!0},_q:[],on:function(e,n){var o=this;setTimeout(function(){n(o[e])},0)},addTest:function(e,n,o){tests.push({name:e,fn:n,options:o})},addAsyncTest:function(e){tests.push({name:null,fn:e})}},Modernizr=function(){};Modernizr.prototype=ModernizrProto,Modernizr=new Modernizr,Modernizr.addTest("customprotocolhandler",function(){if(!navigator.registerProtocolHandler)return!1;try{navigator.registerProtocolHandler("thisShouldFail")}catch(e){return e instanceof TypeError}return!1}),Modernizr.addTest("arrow",function(){try{eval("()=>{}")}catch(e){return!1}return!0});var docElement=document.documentElement;Modernizr.addTest("contextmenu","contextMenu"in docElement&&"HTMLMenuItemElement"in window);var isSVG="svg"===docElement.nodeName.toLowerCase(),CSS=window.CSS;Modernizr.addTest("cssescape",CSS?"function"==typeof CSS.escape:!1),testRunner(),setClasses(classes),delete ModernizrProto.addTest,delete ModernizrProto.addAsyncTest;for(var i=0;i<Modernizr._q.length;i++)Modernizr._q[i]();window.Modernizr=Modernizr}(window,document);