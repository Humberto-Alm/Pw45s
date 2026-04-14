# NetCrud - API de Gerenciamento de Produtos

Esta é uma aplicação de exemplo desenvolvida em **.NET 9** [cite: 1], utilizando uma arquitetura de Web API para realizar operações de CRUD (Create, Read, Update, Delete) em um catálogo de produtos.

## 🚀 Sobre o Framework .NET

O **.NET** é uma plataforma de desenvolvimento de código aberto, mantida pela Microsoft e pela comunidade, utilizada para criar diversos tipos de aplicações (Web, Mobile, Desktop, Jogos e IoT). No caso deste projeto, utilizamos o **ASP.NET Core**, que é a vertente otimizada para a nuvem e alto desempenho de servidores web.

### Vantagens e Desvantagens

| Característica | Detalhes |
| :--- | :--- |
| **Vantagens** | Alta performance (uma das mais rápidas do mercado), multiplataforma (roda em Windows, Linux e macOS), tipagem forte (C#), ecossistema gigante via NuGet e excelente suporte a ferramentas de produtividade (VS Code, Visual Studio). |
| **Desvantagens** | Curva de aprendizado inicial pode ser considerada íngreme para iniciantes devido à complexidade do ecossistema e o tamanho do runtime/SDK é superior a alternativas como Node.js. |

### Principais Características
* **Injeção de Dependência Nativa:** Facilita a manutenção e testes.
* **Middlewares:** Pipeline de processamento de requisições altamente configurável.
* **Entity Framework Core:** ORM poderoso para manipulação de dados (utilizado com SQLite neste projeto).
* **Suporte a OpenAPI/Scalar:** Documentação automática e interativa da API.

---

## 🛠️ Detalhes Técnicos

### Servidores Web Disponíveis
1.  **Kestrel:** Servidor interno de alta performance que acompanha o .NET (utilizado por padrão).
2.  **IIS (Internet Information Services):** Servidor robusto da Microsoft para ambientes Windows.
3.  **Nginx / Apache:** Comumente utilizados como proxies reversos à frente do Kestrel em ambientes Linux.

### Configurações Necessárias
Para rodar esta aplicação, você precisará de:
* **SDK do .NET 9.0** ou superior [cite: 1].
* Uma IDE ou editor de texto (VS Code, Visual Studio ou JetBrains Rider).
* Ferramentas de linha de comando (Terminal/PowerShell).

### Licença e Desenvolvimento
* **Licença:** O .NET é licenciado sob as licenças **MIT** e **Apache 2.0** (Open Source).
* **Responsáveis:** Desenvolvido primordialmente pela **Microsoft** em conjunto com a **.NET Foundation** e uma comunidade global ativa.

---

## 📖 Tutorial de Configuração e Execução

Siga os passos abaixo para configurar o ambiente e rodar o **NetCrud**.

### 1. Instalação do Framework
Baixe e instale o SDK em: [dotnet.microsoft.com/download](https://dotnet.microsoft.com/download)

### 2. Configuração do Projeto Exemplo
O projeto utiliza um banco de dados **SQLite** chamado `products.db` [cite: 5, 124].

1.  **Restaurar Dependências:**
    No terminal, dentro da pasta do projeto, execute:
    ```bash
    dotnet restore
    ```

2.  **Executar a Aplicação:**
    ```bash
    dotnet run
    ```
    *O sistema verificará se o banco de dados existe e importará os dados iniciais do arquivo `Data/import.sql` automaticamente na primeira execução*.

### 3. Testando a API
A aplicação subirá no endereço padrão definido no arquivo `NetCrud.http`, que é `http://localhost:5071` [cite: 3].

* **Documentação Interativa:** Acesse `http://localhost:5071/scalar/v1` (ou o endpoint configurado) para visualizar e testar os endpoints via **Scalar**.
* **Endpoints Disponíveis:**
    * `GET /products`: Lista todos os produtos (ex: Monitor OLED, Mouse Gamer).
    * `POST /products`: Cria um novo produto.

---

## 📝 Estrutura do Código

* **`Program.cs`**: Ponto de entrada da aplicação, onde são configurados o banco de dados SQLite, a injeção de dependência do repositório e o pipeline do Scalar.
* **`NetCrud.csproj`**: Gerencia as bibliotecas do projeto, incluindo Entity Framework e pacotes de OpenAPI [cite: 1, 2].
* **`appsettings.json`**: Configurações de logs e hosts permitidos.
* **`NetCrud.sln`**: Arquivo de solução para gerenciamento do projeto no Visual Studio [cite: 4].

---

## 💡 Conclusão

O uso do framework .NET, especialmente na versão 9, demonstra-se extremamente eficiente para o desenvolvimento de APIs robustas. 

**Facilidade de Configuração:** A configuração é simplificada pelo uso de Minimal APIs e injeção de dependência nativa. A integração com o SQLite permite que o desenvolvedor comece a trabalhar imediatamente sem a necessidade de instalar servidores de banco de dados complexos.

**Qualidade do Material:** O material disponível na documentação oficial (Microsoft Learn) é de altíssima qualidade, sendo um dos ecossistemas mais bem documentados do mundo. A comunidade é vasta, o que facilita a resolução de problemas através de fóruns como StackOverflow e GitHub.