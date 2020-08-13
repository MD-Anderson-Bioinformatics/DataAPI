\name{setGlobalUrl}
\alias{setGlobalUrl}
\title{Set the global URL to change which DAPI server is used.}
\description{Set the global URL to change which DAPI server is used. Use the form: http://localhost:8080/DAPI}
\usage{
setGlobalUrl(theURL)
}
\arguments{
  \item{theURL}{A String. Set the global URL to change which Index API server is used. Use the form: http://localhost:8080/DAPI}
}
\value{Returns results from assign call.}
\examples{
setGlobalUrl(getGlobalUrl())
}
