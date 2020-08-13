class DataAccess_http
{
	constructor(theParent)
	{
		this.parent = theParent;
	};

	addImage(theRequestedId, theImagePath, theDisplayDivId)
	{
		var imgId = theDisplayDivId + "dynamicImg";
		//console.log("imgId=" + imgId);
		$('#' + imgId).remove();
		if (notUN(theImagePath))
		{
			$.ajax(
			{
				type: "GET",
				dataType: 'text',
				url: "dsimage",
				cache: false,
				data:
				{
					id: theRequestedId,
					image: theImagePath
				},
				success: function (theImageData)
				{
					$('#' + theDisplayDivId).append('<div class="displayPngDiv plotChild"><img class="displayPngImg plotChild" id="' + imgId + '" src="" /></div>');
					$("#" + imgId).attr("src", "data:image/gif;base64," + theImageData);
				},
				error: function (jqXHR, textStatus, errorThrown)
				{
					console.log("dsimage" + " :" + textStatus + " and " + errorThrown);
					alert("dsimage" + " :" + textStatus + " and " + errorThrown);
				}
			});
		}
	};

	setIndexAndId()
	{
		var url = new URL(window.location.href);
		var tmpId = url.searchParams.get("id");
		var tmpIndex = url.searchParams.get("index");
		if ((null === tmpId) || (null === tmpIndex))
		{
			return new Promise((resolve, reject) => 
			{
				$.ajax(
				{
					type: "GET",
					dataType: 'json',
					url: "defaults",
					cache: false,
					success: function (theJson)
					{
						resolve(theJson);
					},
					error: function (jqXHR, textStatus, errorThrown)
					{
						console.log("defaults" + " :" + textStatus + " and " + errorThrown);
						alert("defaults" + " :" + textStatus + " and " + errorThrown);
					}
				});
			});
		}
		else
		{
			return new Promise((resolve, reject) => 
			{
				let alg = url.searchParams.get("alg");
				let lvl1 = url.searchParams.get("lvl1");
				let lvl2 = url.searchParams.get("lvl2");
				resolve(
				{
					"mIndexSource": tmpIndex,
					"mID": tmpId,
					"mAlg": alg,
					"mLvl1": lvl1,
					"mLvl2": lvl2
				});
			});
		}
	};
	
	loadLinks()
	{
		return $.ajax(
		{
			type: "GET",
			dataType: 'json',
			url: "urls",
			cache: false,
			error: function (jqXHR, textStatus, errorThrown)
			{
				console.log("urls" + " :" + textStatus + " and " + errorThrown);
				alert("urls" + " :" + textStatus + " and " + errorThrown);
			}
		});
	};

	loadIndexAndId(theRequestedIdKO)
	{
		//console.log("loadIndexAndId = " + theRequestedIdKO());
		return $.ajax(
		{
			type: "GET",
			dataType: 'json',
			url: "dsindex",
			cache: false,
			data:
			{
				id: theRequestedIdKO()
			},
			error: function (jqXHR, textStatus, errorThrown)
			{
				console.log("dsindex" + " :" + textStatus + " and " + errorThrown);
				alert("dsindex" + " :" + textStatus + " and " + errorThrown);
			}
		});
	};
	
	getDataFile(theRequestedId, theTextFile)
	{
		const ajax = $.ajax(
		{
			type: "GET",
			dataType: 'text',
			url: "dstext",
			cache: false,
			data:
			{
				id: theRequestedId,
				text: theTextFile
			}
		});
		ajax.fail(function (jqXHR, textStatus)
		{
			console.log("dstext" + " :" + textStatus);
			alert("dstext" + " :" + textStatus);
		});
		return ajax;
	};
	
	getDataBlobPromise(theRequestedId, theTextFile, theBaseUrl)
	{
		return new Promise((resolve, reject) => 
		{
			var url = theBaseUrl + "/dsblob?" 
					+ "id=" + theRequestedId + "&" + "text=" + theTextFile;
			var xhr = new XMLHttpRequest();
			xhr.open("GET", url);
			xhr.responseType = "blob";
			xhr.onload = () => 
			{
				if (xhr.readyState===4 && xhr.status===200) 
				{
					var e = new Blob([xhr.response], 
					{
						type: "application/zip"
					});
					resolve(e);
				}
			};
			xhr.onerror = () => 
			{
				reject(xhr);
			};
			xhr.send();
		});
	};
}