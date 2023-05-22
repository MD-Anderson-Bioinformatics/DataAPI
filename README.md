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
https://bioinformatics.mdanderson.org/BEV/QF

Java projects are Netbeans 11 projects.
R packages are RStudio projects.

### MBatch Omic Browser Docker Quick Start

Download the docker-compose_MQA.yml file at the root of this repository. This file is setup for use on Linux.

Make the following directories.
 - /MQA/DATA
 - /MQA/indexes
 - /MQA/dsc_indexes
 - /MQA/config

 1. Copy the contents of data/testing_static/MQA/DATA into /MQA/DATA.
 2. Copy the contents of data/testing_static/MQA/INDEXES into /MQA/indexes.
 3. Copy the contents of data/testing_static/MQA/DSC_INDEXES into /MQA/dsc_indexes.
 4. Copy the contents of data/testing_static/MQA/CONFIG into /MQA/config.

Permissions or ownership of the directories may need to be changed or matched to the Docker image user 2002.

In the directory with the docker-compose.yml file run:

	docker compose -p bephub -f docker-compose_MQA.yml up --no-build -d

You can stop it with:

	docker compose -p bephub -f docker-compose_MQA.yml down

To connect to the MBatch Omic Browser with:

	localhost:8080/MQA

### MetaBatch Omic Browser Docker Quick Start

Download the docker-compose_MOB.yml file at the root of this repository. This file is setup for use on Linux.

Make the following directories.

 - MOB/DATA
 - /MOB/indexes
 - /MOB/dsc_indexes
 - /MOB/config

 1. Copy the contents of data/testing_static/MOB/DATA into /MOB/DATA.
 2. Copy the contents of data/testing_static/MOB/INDEXES into /MOB/indexes.
 3. Copy the contents of data/testing_static/MOB/DSC_INDEXES into /MOB/dsc_indexes.
 4. Copy the contents of data/testing_static/MOB/CONFIG into /MOB/config.

Permissions or ownership of the directories may need to be changed or matched to the Docker image user 2002.

In the directory with the docker-compose.yml file run:

	docker compose -p bephub -f docker-compose_MOB.yml up --no-build -d

You can stop it with:

	docker compose -p bephub -f docker-compose_MOB.yml down

To connect to the MBatch Omic Browser with:

	localhost:8080/MtOB


## Links for External Libraries
Fontawesome Icons
https://fontawesome.com/icons?d=gallery&m=free

Datatables JQuery Plug-In
https://datatables.net/

## Install DAPIR
DAPIR has been tested with Java 8 and R 4+ series.
```R
    # required CRAN packages (devtools for install, httr for HTTP processing)
    install.packages(c("devtools", "httr"), dependencies=TRUE, repos = "http://cloud.r-project.org/")
    ## install package
    devtools::install_github("MD-Anderson-Bioinformatics/DataAPI/apps/DAPIR")
```

## Sample R Code for DAPIR
Used to copy Standardized Data locally.
```R
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
```

**For educational and research purposes only.**

**Funding** 
This work was supported in part by U.S. National Cancer Institute (NCI) grant: Weinstein, Mills, Akbani. Batch effects in molecular profiling data on cancers: detection, quantification, interpretation, and correction, 5U24CA210949
This work was supported in part by U.S. National Cancer Institute (NCI) grant: Weinstein, Broom, Akbani. Computational Tools for Analysis and Visualization of Quality Control Issues in Metabolomic Data, U01CA235510

