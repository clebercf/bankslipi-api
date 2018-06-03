# API de boletos #

O objetivo do projeto é disponibilizar uma API REST para geração de boletos que será consumido por um módulo de um sistema de gestão financeira de microempresas.

## Arquitetura 

- Java versão 8
- Stack: Spring Boot
- Build: Maven

## Instalação 

- Maven é o pacote de build adotado no projeto. Para se executar o build, a partir do diretório do projeto (pom.xml), o comando abaixo gerará o pacote do projeto (jar) e adicionalmente executará os testes unitários e integrados. 
 
		mvn clean package
 
- Com o pacote gerado, utiliza-se o java para subir a aplicação, através do seguinte comando:
 
		java -jar target/bankslip-api-0.0.1-SNAPSHOT.jar

## Documentação da API

- O contrato da api esta disponível no link abaixo. Para acessá-lo, basta abrir o mesmo no navegador de internet. Como premissa, o aplicativo deverá estar ativo.

		http://localhost:8080/swagger-ui.html

## Cobertura dos testes
 
 - O projeto possibilita a geração de um relatório de cobertura de testes, assim o desenvolvedor pode se guiar no momento do desenvolvimento dos testes. Para gerar o relatório, basta executar o comando.

		mvn clean cobertura:cobertura
 
- Após o comando, o relatório estará disponível no diretório abaixo. Basta abri-lo no navegador de internet.
 
 		{pasta do projeto}/target/site/jacoco-ut/index.html