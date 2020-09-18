
/* global globalDataAccess, appview, globalDiagramControl, Promise */

notUN = function(theValue)
{
	return ((undefined!==theValue)&&(null!==theValue));
};

function supportsPdf(theAlgorithm)
{
	var result = true;
	if (!notUN(theAlgorithm))
	{
		result = false;
	}
	else if (!notUN(theAlgorithm()))
	{
		result = false;
	}
	else if ("NGCHM" === theAlgorithm().entry_label)
	{
		result = false;
	}
	else if ("Dispersion Separability Criterion" === theAlgorithm().entry_label)
	{
		result = false;
	}
	return result;
}

function toggleClass(theIdArray, theClass)
{
	for (const id of theIdArray)
	{
		var element1 = document.getElementById(id);
		element1.classList.toggle(theClass);
	}
}

function getWithCheck(theJsonObj, theAttr1, theAttr2)
{
	let val = null;
	if (notUN(theJsonObj))
	{
		val = theJsonObj[theAttr1];
		if (notUN(theAttr2))
		{
			val = val[theAttr2];
		}
	}
	return val;
}

function bevDatapaneClear()
{
	$('.dpselect').remove();
}

function bevDatapaneCopyAll()
{
	// clear current selection
	window.getSelection().removeAllRanges();
	// get datapane parent node
	let parent = document.querySelector("#BEVDisplay_Datapane_Content");
	// select all children
	window.getSelection().selectAllChildren(parent);
	// do copy
	document.execCommand('copy');
}

function processZipSelectEvent(theEvent)
{
	//console.log("file changed");
	let files = theEvent.target.files;
	//console.log(files);
	if (files.length>0)
	{
		let file = files[0];
		//console.log(file);
		globalDataAccess.setZipFile(file);
		globalDataAccess.loadIndexAndId().then((theIndexJson) =>
		{
			appview.jsonIndex(theIndexJson);
		});
	}
}

function BEVAppView()
{
	var self = this;
	// used for prettifying initial load
	self.makeGuiVisible = ko.observable(false);
	// used for ZIP archive file buttons
	self.protocol = ko.observable(globalDataAccess.protocol);
	self.zipFile = ko.observable(null);
	/////
	self.appName = ko.observable(getBEVtitle());
	document.title = self.appName();
	//
	// URL parameters
	//
	// selected/requested ID/index prefix for data
	self.requestedId = ko.observable(undefined);
	self.requestedIndex = ko.observable(undefined);
	// JSON index from ZIP archive
	self.jsonIndex = ko.observable(undefined);
	self.jsonIndex.subscribe(function(theNewValue)
	{
		let notice = theNewValue.notice;
		if (notUN(notice))
		{
			if (""!==notice)
			{
				alert(notice);
			}
		}
	});
	// this below only needs to be called once, 
	// since it is setting the KnockoutJS object, 
	// which does not change, only the contents change
	globalDiagramControl.setValues(self.jsonIndex);
	// Level 0 dropdown JSON index selections
	self.algorithmLabel = ko.computed(function ()
	{
		let val = undefined;
		if (notUN(self.jsonIndex()))
		{
			val = self.jsonIndex().mbatch.dropdown_label;
		}
		return val;
	});
	self.algorithmOptions = ko.computed(function ()
	{
		let val = undefined;
		if (notUN(self.jsonIndex()))
		{
			val = self.jsonIndex().mbatch.dropdown_entries;
		}
		return val;
	});
	self.algorithmSelected = ko.observable(undefined);
	if (notUN(self.algorithmOptions()))
	{
		self.algorithmSelected(self.algorithmOptions()[0]);
	};
	// Level 1 dropdown JSON index selections
	self.level1Label = ko.computed(function () 
	{
		var result =  null;
		if(notUN(self.algorithmSelected()))
		{
			if(notUN(self.algorithmSelected().dropdown_label))
			{
				result = self.algorithmSelected().dropdown_label;
			}
		}
		return result;
	});
	self.level1Options = ko.computed(function () 
	{
		var result =  null;
		if(notUN(self.algorithmSelected()))
		{
			if(notUN(self.algorithmSelected().dropdown_entries))
			{
				result = self.algorithmSelected().dropdown_entries;
			}
		}
		return result;
	});
	self.level1Selected = ko.observable(null);
	if (notUN(self.level1Options()))
	{
		self.level1Selected(self.level1Options()[0]);
	}
	// Level 2 dropdown JSON index selections
	self.level2Label = ko.computed(function () 
	{
		var result =  null;
		if(notUN(self.level1Selected()))
		{
			if(notUN(self.level1Selected().dropdown_label))
			{
				result = self.level1Selected().dropdown_label;
			}
		}
		return result;
	});
	self.level2Options = ko.computed(function () 
	{
		var result =  null;
		if(notUN(self.level1Selected()))
		{
			if(notUN(self.level1Selected().dropdown_entries))
			{
				result = self.level1Selected().dropdown_entries;
			}
		}
		return result;
	});
	self.level2Selected = ko.observable(null);
	if (notUN(self.level2Options()))
	{
		self.level2Selected(self.level2Options()[0]);
	}
	globalDiagramControl.setKOVars(self.algorithmOptions, self.algorithmSelected, 
									self.level1Options, self.level1Selected, 
									self.level2Options, self.level2Selected);
	// selected diagram
	self.selectedDiagram = ko.computed(function () 
	{
		var selected = self.level2Selected();
		//console.log("selectedDiagram 2 = " + selected);
		if(!notUN(selected))
		{
			selected = self.level1Selected();
			//console.log("selectedDiagram 1 = " + selected);
			if(!notUN(selected))
			{
				selected = self.algorithmSelected();
				//console.log("selectedDiagram a = " + selected);
			}
		}
		globalDiagramControl.handleNewDiagram(self.requestedId(), selected);
		return selected;
	});
	//
	self.urlQueryForm = ko.observable("");
	self.urlBevForm = ko.observable("");
	self.urlStdData = ko.observable("");
	//
	Promise.all([
		globalDataAccess.loadLinks(),
		globalDataAccess.setIndexAndId()
	]).then((values) =>
	{
		var linksJson = values[0];
		//console.log("linksJson.queryForm");
		//console.log(linksJson.queryForm);
		if (notUN(linksJson.queryForm))
		{
			self.urlQueryForm(linksJson.queryForm);
		}
		//console.log("linksJson.bevForm");
		//console.log(linksJson.bevForm);
		self.urlBevForm(linksJson.bevForm);
		//console.log("linksJson.stdData");
		//console.log(linksJson.stdData);
		if (notUN(linksJson.stdData))
		{
			self.urlStdData(linksJson.stdData);
		}
		var requestedJson = values[1];
		if(notUN(requestedJson))
		{
			self.requestedId(requestedJson.mID);
			self.requestedIndex(requestedJson.mIndexSource);
			// handles URL containing link to specific dataset and diagram
			globalDataAccess.loadIndexAndId(self.requestedId, self.requestedIndex).then((theIndexJson) =>
			{
				self.jsonIndex(theIndexJson);
				if(notUN(requestedJson.mAlg))
				{
					self.algorithmOptions().forEach(function(theOpt)
					{
						if (requestedJson.mAlg===theOpt.entry_label)
						{
							self.algorithmSelected(theOpt);
						}
					});
				}
				if(notUN(requestedJson.mLvl1))
				{
					self.level1Options().forEach(function(theOpt)
					{
						if (requestedJson.mLvl1===theOpt.entry_label)
						{
							self.level1Selected(theOpt);
						}
					});
				}
				if(notUN(requestedJson.mLvl2))
				{
					self.level2Options().forEach(function(theOpt)
					{
						if (requestedJson.mLvl2===theOpt.entry_label)
						{
							self.level2Selected(theOpt);
						}
					});
				}
				self.makeGuiVisible(true);
			});
		}
		else
		{
			self.makeGuiVisible(true);
		}
	});
	//
	self.newTabUrl = ko.computed(function () 
	{
		let tabUrl = null;
		if (self.protocol() !== 'file:')
		{
			var url = new URL(window.location.href);
			var tmpId = url.searchParams.get("id");
			var tmpIndex = url.searchParams.get("index");
			if ((null !== tmpId) && (null !== tmpIndex))
			{
				tabUrl = self.urlBevForm() 
						+ '/?id='+ encodeURIComponent(tmpId) 
						+ '&index='+ encodeURIComponent(tmpIndex);
				if (notUN(self.algorithmSelected()))
				{
					tabUrl = tabUrl + '&alg='+ encodeURIComponent(self.algorithmSelected().entry_label);
				}
				if (notUN(self.level1Selected()))
				{
					tabUrl = tabUrl + '&lvl1='+ encodeURIComponent(self.level1Selected().entry_label);
				}
				if (notUN(self.level2Selected()))
				{
					tabUrl = tabUrl + '&lvl2='+ encodeURIComponent(self.level2Selected().entry_label);
				}
			}
		}
		return tabUrl;
	});


	self.checkIndexForValue = function(theAttribute, theValue)
	{
		var result = false;
		if (notUN(self.jsonIndex()))
		{
			if (theValue===self.jsonIndex()[theAttribute])
			{
				result = true;
			}
		}
		return result;
	};

	self.indexSourceID = ko.computed(function () 
	{
		return getWithCheck(self.jsonIndex(), 'SourceID', null);
	});

	self.indexSource = ko.computed(function () 
	{
		return getWithCheck(self.jsonIndex(), 'source', null);
	});

	self.indexVariant = ko.computed(function () 
	{
		return getWithCheck(self.jsonIndex(), 'variant', null);
	});

	self.indexProject = ko.computed(function () 
	{
		return getWithCheck(self.jsonIndex(), 'project', null);
	});

	self.indexSubProject = ko.computed(function () 
	{
		return getWithCheck(self.jsonIndex(), 'subProject', null);
	});

	self.indexCategory = ko.computed(function () 
	{
		return getWithCheck(self.jsonIndex(), 'category', null);
	});

	self.indexPlatform = ko.computed(function () 
	{
		return getWithCheck(self.jsonIndex(), 'platform', null);
	});

	self.indexData = ko.computed(function () 
	{
		return getWithCheck(self.jsonIndex(), 'data', null);
	});

	self.indexAlgorithm = ko.computed(function () 
	{
		return getWithCheck(self.jsonIndex(), 'algorithm', null);
	});

	self.indexDetails = ko.computed(function () 
	{
		return getWithCheck(self.jsonIndex(), 'details', null);
	});

	self.indexVersion = ko.computed(function () 
	{
		return getWithCheck(self.jsonIndex(), 'version', null);
	});

	self.indexMbatchMBatchID = ko.computed(function () 
	{
		return getWithCheck(self.jsonIndex(), 'mbatch', 'MBatchID');
	});

	self.indexMbatchMBatchRun = ko.computed(function () 
	{
		return getWithCheck(self.jsonIndex(), 'mbatch', 'mbatch_run');
	});

	self.indexMbatchMBatchDatasetType = ko.computed(function () 
	{
		return getWithCheck(self.jsonIndex(), 'mbatch', 'dataset_type');
	});
	
	self.isPngDiagram = ko.observable(false);
	self.algorithmSelected.subscribe(function(theNewValue)
	{
		//console.log("subscribe 1 " + theNewValue.entry_label);
		if ("Supervised Clustering"===theNewValue.entry_label)
		{
			self.isPngDiagram(true);
		}
		//else if ("Correlation Density Plot"===theNewValue.entry_label)
		//{
		//	self.isPngDiagram(true);
		//}
		else
		{
			self.isPngDiagram(false);
		}
	});

} // END model view
