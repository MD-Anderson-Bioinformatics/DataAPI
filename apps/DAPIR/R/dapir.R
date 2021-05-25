# DAPIR Copyright (c) 2011, 2012, 2013, 2014, 2015, 2016, 2017, 2018, 2019, 2020, 2021 University of Texas MD Anderson Cancer Center
#
# This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
#
# This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
#
# MD Anderson Cancer Center Bioinformatics on GitHub <https://github.com/MD-Anderson-Bioinformatics>
# MD Anderson Cancer Center Bioinformatics at MDA <https://www.mdanderson.org/research/departments-labs-institutes/departments-divisions/bioinformatics-and-computational-biology.html>

library(httr)

dapirVersion <- function()
{
  "BEA_VERSION_TIMESTAMP"
}

####
#### Base URL functions
####

mdadataEnv <- new.env()

## PROD URL
assign("globalURL", "https://bioinformatics.mdanderson.org/MBatchOmicBrowser", envir=mdadataEnv)

# export
setGlobalUrl <- function(theURL)
{
  assign("globalURL", theURL, envir=mdadataEnv)
}

# export
getGlobalUrl <- function()
{
  get("globalURL", envir=mdadataEnv)
}

pasteToGlobalURL <- function(theExtension)
{
  paste(getGlobalUrl(), theExtension, sep="")
}

####
#### data list functions
####

headerIndexOf <- function(theHeaders, theTitle)
{
  index <- NULL
  if (length(theHeaders)>0)
  {
    for (count in 1:length(theHeaders))
    {
      myHeader <- theHeaders[[count]]
      if (theTitle == myHeader$title)
      {
        index <- count
      }
    }
  }
  else
  {
    print("return message has no headers")
  }
  if (is.null(index))
  {
    errorCondition(paste("Header title ", theTitle, " not found"))
  }
  index
}

getIdDataframe <- function(theQueryResult)
{
  idIndex <- headerIndexOf(theQueryResult$headers, "ID")
  downloadIndex <- headerIndexOf(theQueryResult$headers, "Data Download")
  versionIndex <- headerIndexOf(theQueryResult$headers, "Version")
  vectorId <- as.vector(unlist(sapply(theQueryResult$data, function(theRow) { theRow[idIndex] })))
  vectorDesc <- as.vector(unlist(sapply(theQueryResult$data, function(theRow) { paste(theRow[3:12], sep=" - ", collapse=" - ")})))
  vectorVersion <- as.vector(unlist(sapply(theQueryResult$data, function(theRow) { theRow[versionIndex] })))
  vectorDownload <- as.vector(unlist(sapply(theQueryResult$data, function(theRow) { theRow[downloadIndex] })))
  data.frame(ID=vectorId, DESC=vectorDesc, VERSION=vectorVersion, DOWNLOAD=vectorDownload, stringsAsFactors=FALSE)
}

getDownloadableData <- function(theQueryRequest)
{
  theURL=pasteToGlobalURL("/query/?search=")
  theURL <- paste(theURL, URLencode(theQueryRequest, reserved=TRUE), sep="")
  response <- GET(theURL)
  print(response)
  stop_for_status(response)
  myContent <- content(response)
  message("headers")
  print(unlist(lapply(myContent$headers,function(theVal) {theVal$title})))
  message("data")
  for(dataOption in myContent$data)
  {
    message("OPTION")
    print(unlist(dataOption))
  }
  getIdDataframe(myContent)
}

getNewDF <- function(theDataDF, theDirVector)
{
  valuesNotInDir <- setdiff(theDataDF$ID, theDirVector)
  theDataDF[theDataDF$ID %in% valuesNotInDir,]
}

getUnlistedDF <- function(theDataDF, theDirVector)
{
  valuesNotInDF <- setdiff(theDirVector, theDataDF$ID)
  valuesNotInDF
}

getUnchangedDF <- function(theDataDF, theDirVector)
{
  valuesUnchanged <- intersect(theDataDF$ID, theDirVector)
  theDataDF[theDataDF$ID %in% valuesUnchanged,]
}

# export
checkDownloadedDataStatus <- function(theQueryRequest, theDownloadDir)
{
  print(dapirVersion())
  dataDF <- getDownloadableData(theQueryRequest)
  dirVector <- list.dirs(theDownloadDir, full.names=FALSE, recursive=FALSE)
  dirVector <- dirVector[dirVector!=""]
  resultList <- list(getNewDF(dataDF, dirVector), getUnlistedDF(dataDF, dirVector), getUnchangedDF(dataDF, dirVector))
  names(resultList) <- c("NEW", "UNLISTED", "UNCHANGED")
  resultList
}

####
#### download functions
####

getArchive <- function(theId, theURL, theDir)
{
  fullpath <- file.path(theDir, paste(theId, ".zip", sep=""))
  download.file(theURL, fullpath)
  fullpath
}

extractArchive <- function(theZip, theExtractDir)
{
  unzip(theZip, exdir=theExtractDir)
}

# export
downloadData <- function(theNewDF, theDownloadDir=getwd())
{
  print(dapirVersion())
  idVector <- theNewDF$ID
  downloadVector <- theNewDF$DOWNLOAD
  for (count in 1:length(downloadVector))
  {
    myID <- idVector[count]
    myURL <- downloadVector[count]
    zip <- getArchive(myID, myURL, theDownloadDir)
    extractArchive(zip, file.path(theDownloadDir, myID))
  }
}

####
#### maintenance functions
####
# export
deleteData <- function(theUnlistedVector, theDownloadDir=getwd())
{
  print(dapirVersion())
  for (myID in theUnlistedVector)
  {
    unlink(file.path(theDownloadDir, myID), recursive=TRUE)
  }
}
