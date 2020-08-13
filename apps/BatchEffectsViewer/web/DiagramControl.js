class DiagramControl
{
	constructor(theDataAccess, theDiagramId, theLegendId, theDatapaneId, theBevUrl)
	{
		this.dataAccess = theDataAccess;
		this.divDiagramId = theDiagramId;
		this.divLegendId = theLegendId;
		this.divDatapaneId = theDatapaneId;
		this.indexKO = null;
		this.resizeUtil = null;
		this.widthKO = null;
		this.mAlgOpt = null;
		this.mAlgSel = null;
		this.mLv1Opt = null;
		this.mLv1Sel = null;
		this.mLv2Opt = null;
		this.mLv2Sel = null;
		this.mBevUrl = theBevUrl;
	};
	
	setKOVars(theAlgOpt, theAlgSel, theLv1Opt, theLv1Sel, theLv2Opt, theLv2Sel)
	{
		this.mAlgOpt = theAlgOpt;
		this.mAlgSel = theAlgSel;
		this.mLv1Opt = theLv1Opt;
		this.mLv1Sel = theLv1Sel;
		this.mLv2Opt = theLv2Opt;
		this.mLv2Sel = theLv2Sel;
	};
	
	getEntries(theDatasetPath)
	{
		var splitted = theDatasetPath.split("/");
		var size = splitted.length;
		return [ splitted[size-3], splitted[size-2], splitted[size-1] ];
	};
	
	getMatchingList(theList, theValue)
	{
		if(notUN(theList))
		{
			for (var index=0; index<theList.length; index++)
			{
				var myOpt = theList[index];
				if (theValue===myOpt.entry_label)
				{
					return myOpt;
				}
			}
		}
		return undefined;
	}
	
	selectDiagram(theDatasetPath)
	{
		console.log("theDatasetPath=" + theDatasetPath);
		// get last three entries (algorith, level 1, level 2)
		var [alg, lv1, lv2] = this.getEntries(theDatasetPath);
		if ("PCA"===alg)
		{
			alg = "PCA+";
		}
		if(alg!==this.mAlgSel().entry_label)
		{
			this.mAlgSel(this.getMatchingList(this.mAlgOpt(), alg));
		}
		if((notUN(this.mLv1Sel()))&&(lv1!==this.mLv1Sel().entry_label))
		{
			this.mLv1Sel(this.getMatchingList(this.mLv1Opt(), lv1));
		}
		if((notUN(this.mLv2Sel()))&&(lv2!==this.mLv2Sel().entry_label))
		{
			this.mLv2Sel(this.getMatchingList(this.mLv2Opt(), lv2));
		}
	};
	
	setWidthKO(theWidthKO)
	{
		this.widthKO = theWidthKO;
	};
	
	// called from within DiagramControl.resize via the index.html body onresize option, to update SVGs
	resize()
	{
		//console.log("DiagramControl::resize called");
		var bbox = document.getElementById(this.divDiagramId).getBoundingClientRect();
		if (null!==this.widthKO)
		{
			this.widthKO(window.innerWidth);
		}
		//console.log("DiagramControl::resize bbox.height =" + bbox.height);
		//console.log("DiagramControl::resize bbox.width =" + bbox.width);
		if (notUN(this.resizeUtil))
		{
			this.resizeUtil.resize();
		}
	};
	
	setValues(theIndexKO)
	{
		this.indexKO = theIndexKO;
	};

	notUN(theValue)
	{
		return ((undefined !== theValue) && (null !== theValue));
	};

	handleNewDiagram(theDatasetId, theNewDiagram)
	{
		//console.log("handleNewDiagram start");
		if (notUN(theNewDiagram))
		{
			if(notUN(theNewDiagram.diagram_type))
			{
				//console.log("handleNewDiagram " + theDatasetId);
				//console.log(theNewDiagram);
				this.resizeFunction = null;
				this.removePlotChildren();
				var diagramType = theNewDiagram.diagram_type;
				//console.log("diagramType = " + diagramType);
				if ("boxplot" === diagramType)
				{
					this.handleNewBoxplot(theDatasetId, theNewDiagram);
				}
				else if ("cdp" === diagramType)
				{
					this.handleNewCdp(theDatasetId, theNewDiagram);
				}
				else if ("DSC" === diagramType)
				{
					this.handleNewDsc(theDatasetId, theNewDiagram);
				}
				else if ("hc" === diagramType)
				{
					this.handleNewHierClust(theDatasetId, theNewDiagram);
				}
				else if ("ngchm" === diagramType)
				{
					this.handleNewNgchm(theDatasetId, theNewDiagram, this.mBevUrl);
				}
				else if ("pca" === diagramType)
				{
					this.handleNewPca(theDatasetId, theNewDiagram);
				}
				else if ("sc" === diagramType)
				{
					this.handleNewSuperClust(theDatasetId, theNewDiagram);
				}
				else if ("discrete" === diagramType)
				{
					this.handleNewDiscrete(theDatasetId, theNewDiagram);
				}
				else if ("mutbatch" === diagramType)
				{
					this.handleNewMutbatch(theDatasetId, theNewDiagram);
				}
				else
				{
					console.log("Unknown diagram type:" + diagramType);
					console.log(theNewDiagram);
				}
				// do not use async, call in finished called for diagrams
				// this.resize();
			}
		}
	};
	
	removePlotChildren()
	{
		$('.plotChild').remove();
	};
	
	handleNewMutbatch(theDatasetId, theNewDiagram)
	{
		this.dataAccess.addImage(theDatasetId, theNewDiagram.diagram_image, this.divDiagramId);
		this.dataAccess.addImage(theDatasetId, undefined, this.divLegendId);
	};

	handleNewDiscrete(theDatasetId, theNewDiagram)
	{
		this.dataAccess.addImage(theDatasetId, theNewDiagram.diagram_image, this.divDiagramId);
		this.dataAccess.addImage(theDatasetId, undefined, this.divLegendId);
	};

	handleNewBoxplot(theDatasetId, theNewDiagram)
	{
		//console.log("handleNewBoxplot");
		// clear any existing images -- clear calls are syncronous
		this.dataAccess.addImage(theDatasetId, undefined, this.divDiagramId);
		this.dataAccess.addImage(theDatasetId, undefined, this.divLegendId);
		var ub = new UtilBoxplot(this.dataAccess, this.divDiagramId, this.divLegendId, this.divDatapaneId, theNewDiagram, this.indexKO, theDatasetId);
		ub.newBoxplot(this.makeDataPointLog);
		this.resizeUtil = ub;
		//this.dataAccess.addImage(theDatasetId, theNewDiagram.diagram_image, this.divDiagramId);
		//this.dataAccess.addImage(theDatasetId, theNewDiagram.legend_image, this.divLegendId);
	};

	handleNewCdp(theDatasetId, theNewDiagram)
	{
		// load calls for an image are asyncronous
		this.dataAccess.addImage(theDatasetId, theNewDiagram.diagram_image, this.divDiagramId);
		this.dataAccess.addImage(theDatasetId, theNewDiagram.legend_image, this.divLegendId);
	};

	handleNewDsc(theDatasetId, theNewDiagram)
	{
		// clear any existing images -- clear calls are syncronous
		this.dataAccess.addImage(theDatasetId, undefined, this.divDiagramId);
		this.dataAccess.addImage(theDatasetId, undefined, this.divLegendId);
		var ub = new UtilDsc(this.dataAccess, this.divDiagramId, this.divLegendId, this.divDatapaneId, theNewDiagram, this.indexKO, theDatasetId);
		ub.newDsc();
		this.resizeUtil = ub;
		// use undefined to remove images from previous diagram
		//this.dataAccess.addImage(theDatasetId, undefined, this.divDiagramId);
		//this.dataAccess.addImage(theDatasetId, undefined, this.divLegendId);
	};

	handleNewHierClust(theDatasetId, theNewDiagram)
	{
		// clear any existing images -- clear calls are syncronous
		this.dataAccess.addImage(theDatasetId, undefined, this.divDiagramId);
		this.dataAccess.addImage(theDatasetId, undefined, this.divLegendId);
		var ub = new UtilHierClust(this.dataAccess, this.divDiagramId, this.divLegendId, this.divDatapaneId, theNewDiagram, this.indexKO, theDatasetId);
		ub.newHierClust(this.makeDataPointLog);
		this.resizeUtil = ub;
		//this.dataAccess.addImage(theDatasetId, theNewDiagram.diagram_image, this.divDiagramId);
		//this.dataAccess.addImage(theDatasetId, theNewDiagram.legend_image, this.divLegendId);
	};

	handleNewNgchm(theDatasetId, theNewDiagram, theBevUrl)
	{
		// clear any existing images -- clear calls are syncronous
		this.dataAccess.addImage(theDatasetId, undefined, this.divDiagramId);
		this.dataAccess.addImage(theDatasetId, undefined, this.divLegendId);
		var ub = new UtilNgchm(this.dataAccess, this.divDiagramId, this.divLegendId, this.divDatapaneId, theNewDiagram, this.indexKO, theDatasetId, this.makeDataPointLog, theBevUrl);
		ub.newNgchm();
		this.resizeUtil = ub;
		// use undefined to remove images from previous diagram
		//this.dataAccess.addImage(theDatasetId, undefined, this.divDiagramId);
		//this.dataAccess.addImage(theDatasetId, undefined, this.divLegendId);
	};

	handleNewPca(theDatasetId, theNewDiagram)
	{
		// clear any existing images -- clear calls are syncronous
		this.dataAccess.addImage(theDatasetId, undefined, this.divDiagramId);
		this.dataAccess.addImage(theDatasetId, undefined, this.divLegendId);
		var ub = new UtilPca(this.dataAccess, this.divDiagramId, this.divLegendId, this.divDatapaneId, theNewDiagram, this.indexKO, theDatasetId);
		ub.newPca(this.makeDataPointLog);
		this.resizeUtil = ub;
		//this.dataAccess.addImage(theDatasetId, theNewDiagram.diagram_image, this.divDiagramId);
		//this.dataAccess.addImage(theDatasetId, theNewDiagram.legend_image, this.divLegendId);
	};

	handleNewSuperClust(theDatasetId, theNewDiagram)
	{
		// load calls for an image are asyncronous
		this.dataAccess.addImage(theDatasetId, theNewDiagram.diagram_image, this.divDiagramId);
		this.dataAccess.addImage(theDatasetId, theNewDiagram.legend_image, this.divLegendId);
	};
	
	makeDataPointLog(struct, dataNode)
	{
		if (struct.length > 0)
		{
			var dataPointText = "<p class='barlabel plotChild dpselect'><b class='barlabelbold'>[" + Date().split("GMT")[0] + "]</b><br>";
			for (var i = 0; i < struct.length; i++) {
				for (var j = 0; j < struct[i].length; j++) {
					if (struct[i][0] === "Sample:" || struct[i][0] === "Name:")
					{
						dataPointText += "<b class='barlabelbold'>" + struct[i][0] + "&nbsp;" + struct[i][1] + "</b>";
						j = struct[i].length;
					}
					else
					{
						dataPointText += "&nbsp;" + struct[i][j];
					}
				}
				dataPointText += "<br>";
			}
			dataPointText += "</p>";
			dataNode.innerHTML += dataPointText;
			dataNode.scrollTop = dataNode.scrollHeight;
		}
	};
}