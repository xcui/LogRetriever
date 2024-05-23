# LogRetriever

## Overview
LogRetriever is a Spring Boot-based RESTful service designed to provide on-demand monitoring and retrieval of log files from Unix-based servers.
The service allows users to issue REST requests to fetch and filter log entries from the /var/log directory on the server. This eliminates the
need to manually log into each machine and inspect log files individually.

## Features
* **REST API Endpoints**: Expose endpoints to retrieve log entries from specified log files.
* **Query Parameters**: Support for specifying filename, number of log entries to retrieve, and filtering results based on keyword matches.
* **Performance**: Efficiently handles large log files (over 1GB) ensuring quick access to the latest log entries.
* **Error Handling**: Robust error handling for scenarios such as file not found or read errors.
* **Scalability**: Designed to handle concurrent requests and scalable for large-scale deployments.

## How to Run

### Prerequisites

- Docker

### Clone the repository
```sh
git clone https://github.com/xcui/LogRetriever.git
cd LogRetriever
```

### Build the Docker Image

```sh
docker build -t logretriever .
```

### Run the Docker Container

#### Using a Bind Mount

```sh
docker run -p 8080:8080 -v /path/to/host/log:/var/log logretriever
```

#### Using a Docker Volume
1. Create a Docker Volume
```sh
docker volume create logvolume
```
2. Run the container with the volume
```sh
docker run -p 8080:8080 -v logvolume:/var/log logretriever
```

### Access the API
The API will be accessible at `http://localhost:8080`.

### API Endpoints

#### Get Logs
* URL: `/logs`
* Method: `GET`
* Query Parameters:
  * `filename` (required): The name of the log file in `/var/log`.
  * `numberOfLines` (optional): The number of log lines to retrieve.
  * `filter` (optional): A keyword to filter log lines.

Example request:
```shell
curl "http://localhost:8080/logs?filename=example.log&numberOfLines=10&filter=ERROR"
```

## Low Level Implementation Highlights
Core file seek and log retrieval logic lie in class `LogService`. The approach used to seek file is based on `RandomAccessFile` which 
helps ensure the read starts from file end rather than going through all log lines till the end. This would guarantee performance
efficiency in cases where file size is large. Adding more notes here, if you look at commit history, originally I used `BufferedReader`
which reads line by line from file start. That helped me validate framework of Restful API changes and ensure a happy path for small
files. Then a commit to optimize followed by replacing `BufferedReader` with `RandomAccessFile`.

## Future work
* Further improve scalability by fine-tuning configurations such as threadpool size, connection timeout, session timeout etc.
* Enhance the service to handle log retrieval asynchronously, allowing non-blocking operations and better performance under high load.
* Achieve a better observability posture by adding logs, metrics.
* Add pagination to provide better client-side performance when handling large datasets.
* Support chunking to stream large log files to clients without overwhelming memory.
* Add authentication, authorization, rate limiting to enhance security posture.
* Introduce caching mechanism for frequent accessed log lines/files to reduce I/O operations and improve response time.
* ...