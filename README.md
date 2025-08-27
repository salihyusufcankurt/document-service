# Document Service

A Spring Boot microservice for generating DOCX and PDF documents from
templates using **docx-stamper** and **LibreOffice (JODConverter)**.\
It supports saving the rendered files to **MinIO/S3** storage and
returning presigned download URLs.

------------------------------------------------------------------------

## Features

-   Fill `.docx` templates with dynamic data using SpEL expressions
    (`${...}`).
-   Render and download outputs as:
    -   **DOCX**
    -   **PDF** (via LibreOffice)
    -   **ZIP bundle** (containing both DOCX & PDF)
-   External template directory support (`application.properties`).
-   File storage with MinIO/S3 and presigned links.
-   Swagger/OpenAPI auto-docs at `/swagger-ui.html`.

------------------------------------------------------------------------

## Profiles & Requirements

### Development (local)

-   **LibreOffice** must be installed locally.
-   Configure path in `application.properties`:

``` properties
# Local LibreOffice setup
jodconverter.local.enabled=true
jodconverter.local.office-home=C:\\Program Files\\LibreOffice
jodconverter.local.port-numbers=2002
jodconverter.local.max-tasks-per-process=10
jodconverter.remote.enabled=false
```

### Production (Docker)

-   LibreOffice can run inside a Docker container (remote).
-   Example:

``` properties
# Remote LibreOffice setup
jodconverter.local.enabled=false
jodconverter.remote.enabled=true
jodconverter.remote.url=http://libreoffice:2002
```

------------------------------------------------------------------------

## MinIO Setup (local)

1.  Download [MinIO](https://min.io/) binary or run via Docker.

    ``` bash
    mkdir C:\minio\data
    setx MINIO_ROOT_USER minioadmin
    setx MINIO_ROOT_PASSWORD minioadmin
    .\minio.exe server C:\minio\data --console-address ":9001"
    ```

    Console: http://127.0.0.1:9001

2.  Create a bucket, e.g. `my-doc-bucket`.

3.  Configure in `application.properties`:

``` properties
storage.s3.bucket=my-doc-bucket
storage.s3.region=eu-central-1
storage.s3.endpointOverride=http://127.0.0.1:9000
storage.s3.pathStyleAccess=true
storage.s3.accessKey=minioadmin
storage.s3.secretKey=minioadmin
```

------------------------------------------------------------------------

## Template Directory

You can load templates from your Desktop:

``` properties
templates.dir=${user.home}\\Desktop\\templates
```

Each template must be a `.docx` file (e.g. `offer-v1.docx`).

------------------------------------------------------------------------

## Endpoints

### Health Check

    GET /healthz

### Render DOCX

    POST /v1/documents/docx/render

### Render PDF

    POST /v1/documents/pdf/render

### Render ZIP (docx + pdf)

    POST /v1/documents/pdf/bundle

------------------------------------------------------------------------

## Postman Collection

The included [Postman
collection](document-service%20API.postman_collection.json) provides
ready-to-use examples.

Example request body:

``` json
{
  "templateId": "offer-v1",
  "data": {
    "customer": { "name": "Ayşe Yılmaz" },
    "items": [
      { "desc": "Yıllık Lisans", "qty": 1, "price": 12000 },
      { "desc": "Kurulum", "qty": 1, "price": 3500 }
    ],
    "total": 15500
  }
}
```

------------------------------------------------------------------------

## Example Template

Inside your `.docx` template use:

    Tarih: ${issueDate}
    Sayın ${customer.name},

    ${itemsText}

    Toplam: ${totalText}

Values (`issueDate`, `customer.name`, `itemsText`, `totalText`) are
filled dynamically at runtime.

------------------------------------------------------------------------

## Running

``` bash
mvn spring-boot:run
```

Swagger UI: <http://localhost:8086/swagger-ui.html>

------------------------------------------------------------------------

## License

Internal use only.
