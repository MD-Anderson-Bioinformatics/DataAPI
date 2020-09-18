/* global globalDataAccess */

class UtilNgchm
{
	constructor(theDataAccess, theDiagramId, theLegendId, theDatapaneId, theNewDiagram, theIndexKO, theDatasetId,
				theMakeDataPointLogFunction, theBevUrl)
	{
		//console.log("UtilNgchm this");
		//console.log(this);
		//console.log("UtilNgchm theMakeDataPointLogFunction");
		//console.log(theMakeDataPointLogFunction);
		this.dataAccess = theDataAccess;
		this.divDiagramId = theDiagramId;
		this.divLegendId = theLegendId;
		this.divDatapaneId = theDatapaneId;
		this.newDiagram = theNewDiagram;
		this.indexKO = theIndexKO;
		this.datasetId = theDatasetId;
		this.makeDataPointLogFunction = theMakeDataPointLogFunction;
		this.bevUrl = theBevUrl;
	};
	
	// this is a required function, called from within DiagramControl.resize via the body onresize option, to update SVGs
	resize()
	{
		// not used here
	};

	showNgcmDatapoint(e) 
	{
		if (!e.data)
		{
			console.log ('Map details message not valid');
			return;
		}
		else if(e.data.msg !== "ShowCovarDetail")
		{
			var	struct = [ ["Value:", e.data.data.value], ['Row:', e.data.data.rowLabel], ['Column:', e.data.data.colLabel] ];
			var covariates = e.data.data.colCovariates;
			for (var x in covariates)
			{
				var indx = 3+parseInt(x);
				struct[indx] = [];
				struct[indx][0] = covariates[x].name+":";
				struct[indx][1] = covariates[x].value;
			}
			var dataNode = document.getElementById(e.data.id);
			//console.log("showNgcmDatapoint this");
			//console.log(this);
			//console.log("showNgcmDatapoint this.makeDataPointLogFunction");
			//console.log(this.makeDataPointLogFunction);
			this.makeDataPointLogFunction(struct, dataNode);
		}
		else
		{
			var legendTable = document.createElement('table');
			legendTable.classList.add("plotChild");
			legendTable.innerHTML = e.data.data.split('class="chmTblRow"').join('class="chmTblRow-legend"').split('class="color-box"').join('class="color-box color-box-legend"');
			var legend = document.getElementById(e.data.id).parentElement.parentElement.parentElement.getElementsByClassName('legendTop')[0].getElementsByClassName('flexInteriorWrapper')[0];
			if (legend.hasChildNodes())
			{
				legend.removeChild(legend.firstElementChild);
			}
			legend.appendChild(legendTable);
		}
	}
	
	newNgchm()
	{
		$("body").addClass("wait");
		var self = this;
		try
		{
			//console.log("newNgchm self");
			//console.log(self);
			// "ngchm": "/NGCHM/ShipDate_ngchm.ngchm"
			var ngchmFile = this.newDiagram.ngchm;
			var ngchm = document.createElement('iframe');
			ngchm.style = "height:100%; width:100%; border-style:none; ";
			// Make visible and append to DOM
			ngchm.classList.remove("ngchmHidden");
			ngchm.classList.add("ngchmVisible");
			ngchm.classList.add("plotChild");
			var plotDiv = document.getElementById(self.divDiagramId);
			plotDiv.appendChild(ngchm);
			//var ngchmSuffixStr = (ngchmSuffix++).toString();
			// support for multiple internal tags
			// // self.dataNode.id = "ngchmLog_" + ngchmSuffixStr;
			//self.dataNode.id = "ngchmLog_";
			var doc = ngchm.contentWindow.document;
			doc.open();
			if (document.querySelector('[data-src="lib/ngchmWidget-min.js"]') !== null)
			{
				console.log("NGCHM doc.write for file based access");
				globalDataAccess.getDataBlobPromise(self.datasetId, ngchmFile, self.bevUrl).then((blob) => 
				{
					//blob.type = "application/zip";
					//console.log("write blob");
					//console.log(blob);
					var reader = new FileReader();
					reader.readAsDataURL(blob);
					reader.onloadend = function ()
					{
						//console.log("write reader.result");
						//console.log(reader.result);
						// set type, which is empty from jszip blob
						var dataUrl = "data:application/zip;base64," + reader.result.substr(reader.result.indexOf(',')+1);
						//console.log("write dataUrl");
						//console.log(dataUrl);
						doc.write("<!DOCTYPE html>"
								+ "<HTML>"
								+ "<HEAD>"
								+ "</HEAD>"
								+ "<BODY>"
								+ "<div id='NGCHMEmbed' style='display: flex; flex-direction: column; background-color: white; height: 95%; margin-bottom: 0.25em; padding: 5px'></div>" 
								+ "<script type='text/javascript'>" + document.querySelector('[data-src="lib/ngchmWidget-min.js"]').innerHTML + "<\/script>" 
								+ "<script type='text/javascript'> "
									+"var dataUrl =\"" + dataUrl + "\"; "
									//+"console.log('script dataUrl'); "
									//+"console.log(dataUrl); "
									+"fetch(dataUrl).then(res => res.blob()).then(blob => {NgChm.UTIL.embedCHM(blob)}); "
								+ "<\/script>"
								+ "</BODY>"
								+ "</HTML>");
					};
				});
			}
			else
			{
				console.log("NGCHM doc.write for HTTP access");
				//console.log("self.datasetId=" + self.datasetId);
				//console.log("ngchmFile=" + ngchmFile);
				//console.log("self.bevUrl=" + self.bevUrl);
				doc.write("<!DOCTYPE html>"
						+ "<HEAD>"
						+ "<script src='BEVAppView.js'><\/script><script src='DataAccess.js'><\/script><script src='DataAccess_http.js'><\/script>"
						+ "</HEAD>"
						+ "<BODY>"
						+ "<div id='NGCHMEmbed' style='display: flex; flex-direction: column; background-color: white; height: 95%; margin-bottom: 0.25em; padding: 5px'></div>" 
						+ "<script src='lib/ngchmWidget-min.js'><\/script>" 
						+ "<script type='text/Javascript'> "
							//+"console.log('http call 1'); "
							+"var foo=new DataAccess(); "
							//+"console.log(foo); "
							//+"console.log(foo.getDataBlobPromise); "
							//+"console.log('http call 2'); "
							+"foo.getDataBlobPromise('" + self.datasetId + "','" + ngchmFile + "','" + self.bevUrl + "').then((blob) => {NgChm.UTIL.embedCHM(blob)}); "
						+"<\/script>"
						+ "</BODY>"
						+ "</HTML>");
			}
			doc.close();
			var nonce = "N";
			//console.log("newNgchm self addEventListener");
			//console.log(self);
			//console.log(self.showNgcmDatapoint);
			window.addEventListener('message', 
				function(theEvent)
				{
					self.showNgcmDatapoint(theEvent);
				}, 
				false);
			var datapaneId = self.divDatapaneId;
			ngchm.onload = (() => 
			{
				console.log ('Sending message to ngchm');
				ngchm.contentWindow.postMessage ({ nonce: nonce, override: 'ShowMapDetail', ngchm_id: datapaneId }, '*');
				$("body").removeClass("wait");
			});
		}
		catch(theExp)
		{
			$("body").removeClass("wait");
			alert(theExp);
		}
	}
	
	finishedCallback()
	{
		//console.log("diagram_boxplot.js - finishedCallback");
	};

	addDivs(theDiagramDiv, theLegendDiv)
	{
		var plotDiv = document.createElement("div");
		plotDiv.classList.add("DscPlottingDiv");
		plotDiv.classList.add("plotChild");
		theDiagramDiv.appendChild(plotDiv);
		//
		var legendSubDiv = document.createElement("div");
		legendSubDiv.classList.add("DscLegendDiv");
		legendSubDiv.classList.add("plotChild");
		theLegendDiv.appendChild(legendSubDiv);

		return [plotDiv, legendSubDiv];
	};

}