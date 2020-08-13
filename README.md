
# Data API

 * Rehan Akbani (owner)
 * Bradley Broom
 * John Weinstein
 * Tod Casasent (developer)

This is for educational and research purposes only.

Samples from large research projects are often processed and run in multiple batches at different times. Because the samples are processed in batches rather than all at once, the data can be vulnerable to systematic noise such as batch effects (unwanted variation between batches) and trend effects (unwanted variation over time), which can lead to misleading analysis results.

DataAPI is a project for accesing and viewing the data and analysis (particulary of Batch Effects) of large datasets in Standardized Data format.

Additional information can be found at http://bioinformatics.mdanderson.org/main/TCGABatchEffects:Overview

|Component|Description|
|--|--|
|DAPIR (R)|R package for downloading Standardized Data|
|DAPI (app)|Standardized Data Browser and BEV Query Form|
|BatchEffectsViewer (app)|Viewer app for BEV ZIP (DAPI used seperately for Query Form)|
|SDB (docker)|Files to create Standardized Data Browser Docker image (uses DAPI app)|
|BEV (docker)|Files to create Batch Effects Viewer Docker image (uses DAPI app and BEV app)|

MD Anderson Cancer Center Standardized Data Browser link
https://bioinformatics.mdanderson.org/StandardizedDataBrowser

MD Anderson Cancer Center Batch Effects Viewer (Query Form) link
https://bioinformatics.mdanderson.org/BEVQF

Java projects are Netbeans 11 projects.
R packages are RStudio projects.

## Links for External Libraries
Fontawesome Icons
https://fontawesome.com/icons?d=gallery&m=free

Datatables JQuery Plug-In
https://datatables.net/

## Install DAPIR
DAPIR has been tested with Java 8 and R 3.6 series.
    # required CRAN packages (devtools for install, httr for HTTP processing)
    install.packages(c("devtools", "httr"), dependencies=TRUE, repos = "http://cloud.r-project.org/")
    ## install package
    devtools::install_github("MD-Anderson-Bioinformatics/DataAPI/apps/DAPIR")

## Sample R Code for DAPIR
Used to mirror Standardized Data locally.
    # TCGA ACC query string from website
    queryFromSite <- "{\"mFiles\":[],\"mSources\":[],\"mVariants\":[\"current\"],\"mProjects\":[\"TCGA\"],\"mSubprojects\":[\"TCGA-ACC\"],\"mCategories\":[],\"mPlatforms\":[],\"mData\":[],\"mAlgorithms\":[],\"mDetails\":[],\"mVersions\":[]}"
    # temp directory
    datasetDir <- file.path(tempdir(), "DAPIR")
    print(datasetDir)
    dir.create(datasetDir, showWarnings=FALSE, recursive=TRUE)
    # get data status
    results <- checkDownloadedDataStatus(queryFromSite, datasetDir)
    print(results)
    
    # Download initial datasets
    newDatasets <- results$NEW
    downloadData(newDatasets, datasetDir)


## Quick Start with Docker:

Clone the GitHub repository with a shallow clone, since you will not be checking anything back in. This clone call grabs the newest version from master.

>git clone --depth 1 https://github.com/MD-Anderson-Bioinformatics/DataAPI.git

### Standardized Data Browser Docker

Edit and rename the Docker Compose Template as described in the documentation 
https://github.com/MD-Anderson-Bioinformatics/DataAPI/blob/master/docs/DataAPI_01A_DockerSDB.pdf

Create and populate the /DAPI directories.

With the resulting docker-compose.yml file, pull the images with:

>docker-compose -f docker-compose.yml pull

Start the container with:

>docker-compose -p EXT -f docker-compose.yml up -d

The EXT may be varied if needed on your system to ensure unique ids for the container.

The Docker Compose container can be stopped using:

>docker-compose -p EXT -f docker-compose.yml down

### Batch Effects Viewer Docker

Edit and rename the Docker Compose Template as described in the documentation 
https://github.com/MD-Anderson-Bioinformatics/DataAPI/blob/master/docs/DataAPI_01B_DockerBEV.pdf

Create and populate the /DAPI and /BEV directories.

With the resulting docker-compose.yml file, pull the images with:

>docker-compose -f docker-compose.yml pull

Start the container with:

>docker-compose -p EXT -f docker-compose.yml up -d

The EXT may be varied if needed on your system to ensure unique ids for the container.

The Docker Compose container can be stopped using:

>docker-compose -p EXT -f docker-compose.yml down


