initializeTooltips = function()
{
	var tooltipsImg = document.querySelectorAll('*[id^="tooltipImage_"]');
	var tooltipsCont = document.querySelectorAll('*[id^="tooltipContent_"]');
	for(i=0; i<tooltipsImg.length; i++)
	{
		//link tooltipImg to tooltipCont by shared value of the respective data attributes
		tippy('#'+tooltipsImg[i].id, {html: '#'+tooltipsCont[i].id, interactive: true, trigger: 'click' });
	}
};

activateTooltip = function(theImageId, theContentId)
{
	tippy('#'+theImageId, {html: '#'+theContentId, interactive: true, trigger: 'click' } );
};

notUN = function(theValue)
{
	return ((undefined!==theValue)&&(null!==theValue));
};

// This is a simple *viewmodel* - JavaScript that defines the data and behavior of your UI
function AppViewModel()
{
	var self = this;
	self.appName = ko.observable(getDAPItitle());
	document.title = self.appName();

	var url = new URL(window.location.href);
	var defaultQueryJSON = url.searchParams.get("default");
	self.defaultQuery = ko.observable("");
	if (null!==defaultQueryJSON)
	{
		self.defaultQuery(JSON.parse(defaultQueryJSON));
	}

	var urlShowParam = url.searchParams.get("show");
	self.urlShowParam = ko.observable("");
	if ("dsc"!==urlShowParam)
	{
		self.urlShowParam("matrix");
	}
	else
	{
		self.urlShowParam("dsc");
	}

	self.makeGuiVisible = ko.observable(false); //.extend({ deferred: true });
	// use array to track populating by calls to server, add these to keep populated until others are added
	self.doneLoading = ko.observableArray(["DSC-min", "DSC-max"]);

	self.populateMinMax = function(theSelected, theEntered, theQuery, theLoadMarker)
	{
		theSelected(theQuery);
		theEntered(theQuery);
		self.doneLoading.remove(theLoadMarker);
	};

	self.populateOptions = function(theOptionsArray, theServlet, theSelectedArray, theURLQueryList, theDefaultList)
	{
		self.doneLoading.push(theServlet); // = ko.observableArray(["DSC-min", "DSC-max"]);
		$.ajax(
		{
			type: "GET",
			dataType:'json',
			async:true,
			url: theServlet,
			cache: false,
			data:
			{
				show: self.urlShowParam()
			},
			success: function(theJson)
			{
				theOptionsArray(theJson);
				// do this here, as selected values need to be set after the options are there
				// TODO: long term, set defaults better
				if (null!==theURLQueryList)
				{
					theSelectedArray(theURLQueryList);
					theOptionsArray.removeAll(theURLQueryList);
				}
				else
				{
					theSelectedArray(theDefaultList);
					theOptionsArray.removeAll(theDefaultList);
				}
				self.doneLoading.remove(theServlet); // = ko.observableArray(["DSC-min", "DSC-max"]);
			},
			error: function(jqXHR, textStatus, errorThrown)
			{
				console.log(theServlet + " :" + textStatus + " and " + errorThrown);
				alert(theServlet + " :" + textStatus + " and " + errorThrown);
				self.doneLoading.remove(theServlet); // = ko.observableArray(["DSC-min", "DSC-max"]);
			}
		});
	};

	function getQueryDefault(theAttribute)
	{
		var result = null;
		if (""!==self.defaultQuery())
		{
			result = self.defaultQuery()[theAttribute];
		}
		return result;
	}

	////////////////////////////////////////////////////////////////
	//// baseURL for BEV
	////////////////////////////////////////////////////////////////
	self.baseURL = ko.observable("");
	$.ajax(
	{
		type: "GET",
		dataType:'text',
		async:true,
		url: "baseURL",
		cache: false,
		success: function(theText)
		{
			//console.log("query" + " :" + JSON.stringify(theJson));
			self.baseURL(theText);
		},
		error: function(jqXHR, textStatus, errorThrown)
		{
			console.log("baseURL" + " :" + textStatus + " and " + errorThrown);
			alert("baseURL" + " :" + textStatus + " and " + errorThrown);
		}
	});

	////////////////////////////////////////////////////////////////
	//// files
	////////////////////////////////////////////////////////////////
	self.optionsFiles = ko.observableArray();
	self.selectedFiles = ko.observableArray();
	// select data with batch files
	//self.populateOptions(self.optionsFiles, "files", self.selectedFiles, getQueryDefault("mFiles"), ["batches.tsv"]);
	// no filtering on files
	self.populateOptions(self.optionsFiles, "files", self.selectedFiles, getQueryDefault("mFiles"), []);

	////////////////////////////////////////////////////////////////
	//// source
	////////////////////////////////////////////////////////////////
	self.optionsSources = ko.observableArray();
	self.selectedSources = ko.observableArray();
	self.populateOptions(self.optionsSources, "sources", self.selectedSources, getQueryDefault("mSources"), []);

	////////////////////////////////////////////////////////////////
	//// variant
	////////////////////////////////////////////////////////////////
	self.optionsVariants = ko.observableArray();
	self.selectedVariants = ko.observableArray();
	self.populateOptions(self.optionsVariants, "variants", self.selectedVariants, getQueryDefault("mVariants"), ["current"]);

	////////////////////////////////////////////////////////////////
	//// project
	////////////////////////////////////////////////////////////////
	self.optionsProjects = ko.observableArray();
	self.selectedProjects = ko.observableArray();
	self.populateOptions(self.optionsProjects, "projects", self.selectedProjects, getQueryDefault("mProjects"), []);

	////////////////////////////////////////////////////////////////
	//// subproject
	////////////////////////////////////////////////////////////////
	self.optionsSubprojects = ko.observableArray();
	self.selectedSubprojects = ko.observableArray();
	self.populateOptions(self.optionsSubprojects, "subprojects", self.selectedSubprojects, getQueryDefault("mSubprojects"), []);

	////////////////////////////////////////////////////////////////
	//// categorie
	////////////////////////////////////////////////////////////////
	self.optionsCategories = ko.observableArray();
	self.selectedCategories = ko.observableArray();
	self.populateOptions(self.optionsCategories, "categories", self.selectedCategories, getQueryDefault("mCategories"), []);

	////////////////////////////////////////////////////////////////
	//// platform
	////////////////////////////////////////////////////////////////
	self.optionsPlatforms = ko.observableArray();
	self.selectedPlatforms = ko.observableArray();
	self.populateOptions(self.optionsPlatforms, "platforms", self.selectedPlatforms, getQueryDefault("mPlatforms"), []);

	////////////////////////////////////////////////////////////////
	//// data
	////////////////////////////////////////////////////////////////
	self.optionsDatas = ko.observableArray();
	self.selectedDatas = ko.observableArray();
	self.populateOptions(self.optionsDatas, "data", self.selectedDatas, getQueryDefault("mData"), []);

	////////////////////////////////////////////////////////////////
	//// algorithm
	////////////////////////////////////////////////////////////////
	self.optionsAlgorithms = ko.observableArray();
	self.selectedAlgorithms = ko.observableArray();
	self.populateOptions(self.optionsAlgorithms, "algorithms", self.selectedAlgorithms, getQueryDefault("mAlgorithms"), []);

	////////////////////////////////////////////////////////////////
	//// detail
	////////////////////////////////////////////////////////////////
	self.optionsDetails = ko.observableArray();
	self.selectedDetails = ko.observableArray();
	self.populateOptions(self.optionsDetails, "details", self.selectedDetails, getQueryDefault("mDetails"), []);

	////////////////////////////////////////////////////////////////
	//// version
	////////////////////////////////////////////////////////////////
	self.optionsVersions = ko.observableArray();
	self.selectedVersions = ko.observableArray();
	self.populateOptions(self.optionsVersions, "versions", self.selectedVersions, getQueryDefault("mVersions"), []);

	////////////////////////////////////////////////////////////////
	//// min
	////////////////////////////////////////////////////////////////
	self.selectedOverallDSCmin = ko.observable("");
	self.enteredOverallDSCmin = ko.observable("");
	self.populateMinMax(self.selectedOverallDSCmin, self.enteredOverallDSCmin, getQueryDefault("mOverallDSCmin"), "DSC-min");
 
	////////////////////////////////////////////////////////////////
	//// max
	////////////////////////////////////////////////////////////////
	self.selectedOverallDSCmax = ko.observable("");
	self.enteredOverallDSCmax = ko.observable("");
	self.populateMinMax(self.selectedOverallDSCmax, self.enteredOverallDSCmax, getQueryDefault("mOverallDSCmax"),"DSC-max");

	////////////////////////////////////////////////////////////////
	//// version
	////////////////////////////////////////////////////////////////
	self.optionsOverallDSCpvalue = ko.observableArray();
	self.selectedOverallDSCpvalue = ko.observableArray();
	self.populateOptions(self.optionsOverallDSCpvalue, "overalldscpvalues", self.selectedOverallDSCpvalue, getQueryDefault("mOverallDSCpvalue"), []);

	self.selectChangeInSelected = function(theData, theEvent, theSelectedFiles, theOptionsFiles)
	{
		// remove from selected, put into options
		var index = theEvent.target.selectedIndex;
		if (index>=0)
		{
			var value = theSelectedFiles()[index];
			//console.log("selectChangeInSelected = " + index);
			theSelectedFiles.remove(value);
			theOptionsFiles.push(value);
			theOptionsFiles.sort();
		}
	};

	self.selectChangeInOptions = function(theData, theEvent, theSelectedFiles, theOptionsFiles)
	{
		// remove from options, put into selected
		var index = theEvent.target.selectedIndex;
		if (index>=0)
		{
			var value = theOptionsFiles()[index];
			//console.log("selectChangeInOptions = " + index);
			theOptionsFiles.remove(value);
			theSelectedFiles.push(value);
			theSelectedFiles.sort();
		}
	};

	self.selectChangeInSelectedSingle = function(theData, theEvent, theSelectedFiles, theOptionsFiles)
	{
		// remove from selected, put into options
		var index = theEvent.target.selectedIndex;
		if (index>=0)
		{
			var value = theSelectedFiles()[index];
			//console.log("selectChangeInSelected = " + index);
			theSelectedFiles.remove(value);
			theOptionsFiles.push(value);
			theOptionsFiles.sort();
		}
	};

	self.selectChangeInOptionsSingle = function(theData, theEvent, theSelectedFiles, theOptionsFiles)
	{
		// remove from options, put into selected
		var index = theEvent.target.selectedIndex;
		if (index>=0)
		{
			var value = theOptionsFiles()[index];
			// but first clear any currently selected
			var selVal = theSelectedFiles.pop();
			while( notUN(selVal) )
			{
				theOptionsFiles.push(selVal);
				selVal = theSelectedFiles.pop();
			}
			//
			theOptionsFiles.remove(value);
			theSelectedFiles.push(value);
			theSelectedFiles.sort();
		}
	};
	
	////////////////////////////////////////////////////////////////
	//// jsonQueryString
	////////////////////////////////////////////////////////////////
	self.jsonQueryString = ko.computed(function()
	{
		if ('dsc'===self.urlShowParam())
		{
			return JSON.stringify(
						{
							mFiles:			getJsonFromObservableArray(self.selectedFiles),
							mSources:		getJsonFromObservableArray(self.selectedSources),
							mVariants:		getJsonFromObservableArray(self.selectedVariants),
							mProjects:		getJsonFromObservableArray(self.selectedProjects),
							mSubprojects:	getJsonFromObservableArray(self.selectedSubprojects),
							mCategories:	getJsonFromObservableArray(self.selectedCategories),
							mPlatforms:		getJsonFromObservableArray(self.selectedPlatforms),
							mData:			getJsonFromObservableArray(self.selectedDatas),
							mAlgorithms:	getJsonFromObservableArray(self.selectedAlgorithms),
							mDetails:		getJsonFromObservableArray(self.selectedDetails),
							mVersions:		getJsonFromObservableArray(self.selectedVersions),
							mOverallDSCmin:	getJsonFromObservableDouble(self.selectedOverallDSCmin),
							mOverallDSCmax:	getJsonFromObservableDouble(self.selectedOverallDSCmax),
							mOverallDSCpvalue:	getJsonFromObservableArray(self.selectedOverallDSCpvalue)
						});
		}
		else
		{
			return JSON.stringify(
						{
							mFiles:			getJsonFromObservableArray(self.selectedFiles),
							mSources:		getJsonFromObservableArray(self.selectedSources),
							mVariants:		getJsonFromObservableArray(self.selectedVariants),
							mProjects:		getJsonFromObservableArray(self.selectedProjects),
							mSubprojects:	getJsonFromObservableArray(self.selectedSubprojects),
							mCategories:	getJsonFromObservableArray(self.selectedCategories),
							mPlatforms:		getJsonFromObservableArray(self.selectedPlatforms),
							mData:			getJsonFromObservableArray(self.selectedDatas),
							mAlgorithms:	getJsonFromObservableArray(self.selectedAlgorithms),
							mDetails:		getJsonFromObservableArray(self.selectedDetails),
							mVersions:		getJsonFromObservableArray(self.selectedVersions)
						});
		}
	}, self);
	self.jsonQueryString.extend({ rateLimit: { method: "notifyWhenChangesStop", timeout: 500 } });

	////////////////////////////////////////////////////////////////
	//// category options
	////////////////////////////////////////////////////////////////
	// NOT USED - WILL NEED UPDATE FOR DSC INDEXES IF ADDED BACK
//				self.categoryOptions = ko.observable();

//				self.populateCategoryOptions = ko.computed(function()
//				{
//					console.log("populateCategoryOptions");
//					$.ajax(
//					{
//						type: "GET",
//						dataType:'json',
//						async:true,
//						url: "options",
//						cache: false,
//						data:
//						{
//							search: self.jsonQueryString()
//						},
//						success: function(theJson)
//						{
//							self.optionsFiles(theJson.mFiles);
//							self.optionsSources(theJson.mSource);
//							self.optionsVariants(theJson.mVariant);
//							self.optionsProjects(theJson.mProject);
//							self.optionsSubprojects(theJson.mSubproject);
//							self.optionsCategories(theJson.mCategory);
//							self.optionsPlatforms(theJson.mPlatform);
//							self.optionsDatas(theJson.mData);
//							self.optionsAlgorithms(theJson.mAlgorithm);
//							self.optionsDetails(theJson.mDetail);
//							self.optionsVersions(theJson.mVersion);
//						},
//						error: function(jqXHR, textStatus, errorThrown)
//						{
//							console.log("options" + " :" + textStatus + " and " + errorThrown);
//							alert("options" + " :" + textStatus + " and " + errorThrown);
//						}
//					});
//				}, self);

	////////////////////////////////////////////////////////////////
	//// query results
	////////////////////////////////////////////////////////////////

	function getJsonFromObservableArray(theObsArray)
	{
		var value = theObsArray();
		var result = [];
		if (notUN(value))
		{
			if(0===value.length)
			{
				result = [ ];
			}
			else
			{
				result = ko.toJS(value);
			}
		}
		return result;
	}

	function getJsonFromObservableDouble(theObsDouble)
	{
		var value = theObsDouble();
		if (!notUN(value))
		{
			value = null;
		}
		else if (""===value)
		{
			value = null;
		}
		return value;
	}

	self.queryResultLength = ko.observable("");
	self.populateQueryResults = ko.computed(function()
	{
		//console.log("populateQueryResults");
		// prevent double calls when setting default values
		if ( (self.makeGuiVisible()) && (self.doneLoading().length < 1) )
		{
			var date = new Date();
			var start = date.getTime();
			$.ajax(
			{
				type: "GET",
				dataType:'json',
				async:true,
				url: "query",
				cache: false,
				data:
				{
					search: self.jsonQueryString(),
					show: self.urlShowParam()
				},
				success: function(theJson)
				{
					//console.log("query" + " :" + JSON.stringify(theJson));
					date = new Date();
					var finish = date.getTime();
					console.log("Time for query: " + ((finish-start)/1000.0) + " seconds");
					self.queryResultLength(theJson.data.length);
					for(var i = 0; i < theJson.headers.length; i++)
					{
						var obj = theJson.headers[i];
						if (obj.title==="Download")
						{
							obj.render = downloadUrl;
						}
						else if (obj.title==="View")
						{
							obj.render = viewUrl;
						}
					}
					// destroy: true eleminates the old datatable object
					// order: start with 1, since 0 (ID) is present but hidden
					$('#resultsTable').DataTable(
					{
						destroy: true,
						data: theJson.data,
						columns: theJson.headers,
						order: [[ 1, 'asc' ], [ 2, 'asc' ], [ 3, 'asc' ], [ 4, 'asc' ], 
								[ 5, 'asc' ], [ 6, 'asc' ], [ 7, 'asc' ], [ 8, 'asc' ], 
								[ 9, 'asc' ], [ 10, 'asc' ], [ 11, 'asc' ], [ 12, 'asc' ]]
					} );
				},
				error: function(jqXHR, textStatus, errorThrown)
				{
					console.log("query" + " :" + textStatus + " and " + errorThrown);
					alert("query" + " :" + textStatus + " and " + errorThrown);
				}
			});
		}
	}, self);

	self.checkForValue = function(theQueryEntry, theValue)
	{
		var result = false;
		if (theValue===theQueryEntry.mIndexSource)
		{
			result = true;
		}
		return result;
	};

	self.checkStartsValue = function(theQueryEntry, theValue)
	{
		var result = false;
		if (theQueryEntry.mIndexSource.startsWith(theValue))
		{
			result = true;
		}
		return result;
	};
} //End Appview Model


function copyQueryString()
{
	// based on https://www.w3schools.com/howto/howto_js_copy_clipboard.asp
	var copyText = document.getElementById("dapiCopyQuery");
	var text = appview.jsonQueryString();
	text = text.replace(/\"/g, "\\\"");
	console.log(text);
	copyText.value = text;
	copyText.select();
	//For mobile devices
	copyText.setSelectionRange(0, 99999);
	document.execCommand("copy");
	copyText.blur();
}

function copyURLString()
{
	// based on https://www.w3schools.com/howto/howto_js_copy_clipboard.asp
	var copyText = document.getElementById("dapiCopyURL");
	var text = appview.jsonQueryString();
	var url = window.location.origin + window.location.pathname + "?default=" + encodeURIComponent(text);
	//text = text.replace(/\"/g, "\\\"");
	copyText.value = url;
	copyText.select();
	//For mobile devices
	copyText.setSelectionRange(0, 99999);
	document.execCommand("copy");
	copyText.blur();
}

function updateEnteredSelected(theEntered, theSelected, theElementId)
{
	var input = document.getElementById(theElementId);
	if (input.checkValidity())
	{
		theSelected(theEntered());
	}
	else
	{
		alert("Must be a positive number with 3 or fewer significant digits");
	}
}

function downloadUrl(data,type,row,meta)
{
	return "<a href='" + data + "'>Download Data</a>";
}

function viewUrl(data,type,row,meta)
{
	return "<a target='_blank' href='" + data + "'>View Analysis</a>";
}
