class DataAccess
{
	constructor()
	{
		this.internalDataAccess = null;
		this.protocol = window.location.protocol;
		//console.log("protocol = " + this.protocol);
		if ("file:"===this.protocol)
		{
			console.log("do file based data access");
			this.internalDataAccess = new DataAccess_file(this);
		}
		else if (("http:"===this.protocol)||("https:"===this.protocol))
		{
			console.log("do server based data access");
			this.internalDataAccess = new DataAccess_http(this);
		}
		else
		{
			console.log("Unrecognized protocol: '" + this.protocol + "'");
			alert("Unrecognized protocol: '" + this.protocol + "'");
		}
	};

	addImage(theRequestedId, theImagePath, theDisplayDivId)
	{
		return this.internalDataAccess.addImage(theRequestedId, theImagePath, theDisplayDivId);
	};
	setIndexAndId()
	{
		return this.internalDataAccess.setIndexAndId();
	};
	loadLinks()
	{
		return this.internalDataAccess.loadLinks();
	};
	loadIndexAndId(theRequestedIdKO, theRequestedIndexKO)
	{
		return this.internalDataAccess.loadIndexAndId(theRequestedIdKO, theRequestedIndexKO);
	};
	getDataFile(theRequestedId, theTextFile)
	{
		return this.internalDataAccess.getDataFile(theRequestedId, theTextFile);
	};
	getDataBlobPromise(theRequestedId, theTextFile, theBaseUrl)
	{
		return this.internalDataAccess.getDataBlobPromise(theRequestedId, theTextFile, theBaseUrl);
	};

	notUN(theValue)
	{
		return ((undefined !== theValue) && (null !== theValue));
	};
	
	setZipFile(theLocalZipFileObj, theRequestedId, theJsonIndex)
	{
		this.internalDataAccess.setZipFile(theLocalZipFileObj, theRequestedId, theJsonIndex);
	};
}