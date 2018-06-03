# API de boletos #

O projeto consiste na exposição de uma API Rest para geração de boletos. 

## Arquitetura 

- Java versão 8
- Spring Boot

## Instalação 

- A partir da pasta principal do projeto, gerar o pacote (jar) do projeto através do Maven.
O comando executará os testes unitários e integrado do projeto.
 
		mvn clean package
 
- Após a geração do pacote, deve ser inicializado a aplicação através do comando que segue abaixo:
 
		java -jar target/bankslip-api-0.0.1-SNAPSHOT.jar
 
## Documentação da API

- A API utiliza o padrão de documentação swagger, acessível em:

	http://localhost:8080/swagger-ui.html
