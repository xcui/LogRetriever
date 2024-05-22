# LogRetriever

## How to Run

### Prerequisites

- Docker

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