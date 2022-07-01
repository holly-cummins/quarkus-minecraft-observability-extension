# TODO Applications with Quarkus

![GitHub Workflow Status](https://img.shields.io/github/workflow/status/cescoffier/quarkus-todo-app/Build)


## Imperative Application

```bash
cd quarkus-todo
mvn quarkus:dev
```

Open: http://localhost:8080/

## Reactive Application

This version uses Hibernate Reactive, RESTEasy Reactive and Mutiny.

```bash
cd quarkus-todo-reactive
mvn quarkus:dev
```
Open: http://localhost:8080/



# Running with a real database

In dev and test mode, this application will use [dev services](https://quarkus.io/guides/dev-services) to stand 
up a test database. If you're running in prod mode, it will need an external database. To create it, run:

```bash
docker run  -it --rm=true --name postgres-quarkus-rest-http-crud -e POSTGRES_USER=restcrud \ 
    -e POSTGRES_PASSWORD=restcrud -e POSTGRES_DB=rest-crud \
    -p 5432:5432 postgres:13.1
```
