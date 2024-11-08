# swift-checkin
# Projeto Spring Boot com Maven

Este é um projeto Spring Boot configurado com Maven, utilizando as seguintes dependências principais:

- **Spring Boot Data JPA Starter**: Para integração com o banco de dados via JPA.
- **MySQL Driver**: Para comunicação com o banco de dados MySQL.
- **Spring Boot Test Starter**: Para realizar testes automatizados.
- **Spring Boot Web Starter**: Para criar uma aplicação web com Spring Boot.
- **Jakarta Persistence API (para Spring Boot 3.x)**: Para fornecer APIs de persistência de dados (JPA) no Spring Boot.

## Pré-requisitos

Antes de rodar o projeto, certifique-se de ter o **Maven** e o **Java** instalados no seu sistema.

- [Instalar Maven](https://maven.apache.org/install.html)
- [Instalar Java](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)

### Configurações do servidor, como nome, porta, banco de dados... estão no .\server\src\main\resources\application.properties

### Comando.. Antes de tudo, verificar se está no diretório correto


### Verificar Maven instalado:
mvn -v

### Verificar Java instalado:
java -version

### Compilar e instalar dependencias:
mvn clean install

### Rodar a aplicação(server):
mvn spring-boot:run

### Verificar dependências do projeto:
mvn dependency:tree
