# NetCrud - API de Gerenciamento de Produtos

Esta é uma aplicação de exemplo desenvolvida em **.NET 9**, utilizando uma arquitetura de Web API para realizar operações de CRUD (Create, Read, Update, Delete) em um catálogo de produtos.

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
* **SDK do .NET 9.0** ou superior.
* Uma IDE ou editor de texto (VS Code, Visual Studio ou JetBrains Rider).
* Ferramentas de linha de comando (Terminal/PowerShell).

### Licença e Desenvolvimento
* **Licença:** O .NET é licenciado sob as licenças **MIT** e **Apache 2.0** (Open Source).
* **Responsáveis:** Desenvolvido primordialmente pela **Microsoft** em conjunto com a **.NET Foundation** e uma comunidade global ativa.

---

## 📖 Tutorial: Construindo a Aplicação do Zero

Este tutorial percorre a criação de cada componente da aplicação na ordem recomendada de implementação, explicando a finalidade e o funcionamento de cada classe.

> **Pré-requisito:** Crie um novo projeto Web API com o comando:
> ```bash
> dotnet new webapi -n NetCrud
> cd NetCrud
> ```
> Em seguida, instale os pacotes necessários:
> ```bash
> dotnet add package Microsoft.EntityFrameworkCore --version 9.0.5
> dotnet add package Microsoft.EntityFrameworkCore.Sqlite --version 9.0.5
> dotnet add package Microsoft.EntityFrameworkCore.Design --version 9.0.5
> dotnet add package Scalar.AspNetCore
> ```

---

### 1. Model — `Model/Product.cs`

O **Model** é a representação direta de uma entidade do domínio da aplicação. Ele define a estrutura dos dados que serão manipulados e persistidos no banco de dados. No Entity Framework Core, cada propriedade da classe corresponde a uma coluna na tabela do banco.

```csharp
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace NetCrud.Model;

public class Product
{
    [Key]
    [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
    public int Id { get; set; }

    [Required]
    public string Name { get; set; } = string.Empty;

    [Required]
    public decimal Price { get; set; }

    [Required]
    public int Quantity { get; set; }
}
```

**Como funciona:**

- `[Key]` — Marca a propriedade `Id` como chave primária da tabela.
- `[DatabaseGenerated(DatabaseGeneratedOption.Identity)]` — Instrui o banco de dados a gerar o valor do `Id` automaticamente a cada novo registro inserido (auto-increment). Isso elimina a necessidade de controlar manualmente o próximo ID disponível.
- `[Required]` — Define que o campo não pode ser nulo no banco de dados, adicionando uma restrição de NOT NULL à coluna correspondente.
- `= string.Empty` — Inicializa `Name` com uma string vazia para evitar warnings de nullable do compilador do .NET 9, que tem `<Nullable>enable</Nullable>` ativo por padrão.

---

### 2. DTO — `DTO/ProductDto.cs`

O **DTO (Data Transfer Object)** é um objeto simples utilizado para trafegar dados entre o cliente e a API. Ele expõe apenas os campos que o usuário deve enviar em uma requisição, sem incluir campos gerenciados internamente pelo servidor (como o `Id`).

```csharp
namespace NetCrud.DTO;

public class ProductDto
{
    public string Name { get; set; } = string.Empty;
    public decimal Price { get; set; }
    public int Quantity { get; set; }
}
```

**Como funciona:**

Sem o DTO, o cliente poderia enviar um `Id` no corpo da requisição e tentar sobrescrever registros indevidamente. Ao utilizar o DTO nas rotas de criação e atualização, garantimos que o `Id` nunca venha do cliente — ele é sempre gerado pelo banco (no `Create`) ou lido da URL (no `Update`). O Controller recebe o `ProductDto` e constrói um `Product` a partir dele antes de repassar ao repositório.

---

### 3. Data — `Data/AppDbContext.cs`

O **DbContext** é a classe central do Entity Framework Core. Ela representa a sessão com o banco de dados e expõe os `DbSet<T>`, que são as coleções mapeadas para as tabelas. É através dele que todas as operações de leitura e escrita são realizadas.

```csharp
using Microsoft.EntityFrameworkCore;
using NetCrud.Model;

namespace NetCrud.Data;

public class AppDbContext : DbContext
{
    public AppDbContext(DbContextOptions<AppDbContext> options) : base(options) { }

    public DbSet<Product> Products { get; set; } = null!;
}
```

**Como funciona:**

- O construtor recebe `DbContextOptions<AppDbContext>` via injeção de dependência. Essas opções são configuradas no `Program.cs` (definindo o banco SQLite, por exemplo) e repassadas para a classe base `DbContext`.
- `DbSet<Product> Products` — Representa a tabela `Products` no banco. O EF Core utiliza esse mapeamento para gerar a SQL e criar a tabela automaticamente com `EnsureCreated()`.
- `= null!` — O operador `null!` (null-forgiving) é necessário porque o `<Nullable>enable</Nullable>` do projeto exigiria uma inicialização, mas o EF Core preenche esse valor em tempo de execução. O `!` instrui o compilador a ignorar o aviso.

---

### 4. Seed — `Data/import.sql`

O arquivo de seed contém os dados iniciais que serão inseridos no banco de dados na primeira execução do servidor, quando a tabela `Products` ainda estiver vazia.

```sql
INSERT INTO Products (Name, Price, Quantity) VALUES ('Teclado Mecânico', 299.99, 10);
INSERT INTO Products (Name, Price, Quantity) VALUES ('Mouse Gamer', 189.99, 15);
```

**Como funciona:**

O arquivo é um SQL puro executado diretamente pelo `Program.cs` através do método `ExecuteSqlRaw()`. Para que o arquivo esteja disponível na pasta de execução após o build, é necessário declará-lo no `NetCrud.csproj`:

```xml
<ItemGroup>
  <Content Include="Data\import.sql">
    <CopyToOutputDirectory>PreserveNewest</CopyToOutputDirectory>
  </Content>
</ItemGroup>
```

A diretiva `PreserveNewest` garante que o arquivo seja copiado para a pasta de saída somente quando tiver sido modificado, evitando cópias desnecessárias a cada build.

---

### 5. Repository — `Repositories/ProductRepository.cs`

O **Repository** é responsável por encapsular toda a lógica de acesso ao banco de dados. Ele abstrai as operações do EF Core, expondo métodos simples para o Controller. Isso separa a responsabilidade de persistência da lógica de rotas HTTP.

```csharp
using Microsoft.EntityFrameworkCore;
using NetCrud.Data;
using NetCrud.Model;

namespace NetCrud.Repositories;

public class ProductRepository
{
    private readonly AppDbContext _context;

    public ProductRepository(AppDbContext context)
    {
        _context = context;
    }

    // ... métodos abaixo
}
```

O `AppDbContext` é recebido via injeção de dependência no construtor e armazenado como `readonly`, garantindo que a mesma instância seja usada durante todo o ciclo de vida da requisição.

---

#### `GetAll()`

```csharp
public IEnumerable<Product> GetAll() => _context.Products.ToList();
```

Retorna todos os produtos da tabela. O `.ToList()` executa a query SQL imediatamente (`SELECT * FROM Products`) e materializa os resultados em memória, evitando que a execução fique em aberto (deferred execution).

---

#### `GetById(int id)`

```csharp
public Product? GetById(int id) => _context.Products.Find(id);
```

Busca um único produto pela chave primária. O método `.Find()` é otimizado para buscas por `Id`: antes de ir ao banco, ele verifica se o objeto já está rastreado no contexto (cache de primeiro nível), evitando uma query desnecessária.

O tipo de retorno `Product?` (nullable) indica que o método pode retornar `null` caso nenhum produto com o `id` informado seja encontrado.

---

#### `Create(Product product)`

```csharp
public Product Create(Product product)
{
    _context.Products.Add(product);
    _context.SaveChanges();
    return product;
}
```

Adiciona o produto ao rastreamento do EF Core com `.Add()` e persiste a inserção no banco com `.SaveChanges()`, que gera e executa o `INSERT INTO`. Após o `SaveChanges()`, o EF Core preenche automaticamente a propriedade `Id` do objeto com o valor gerado pelo banco, e o objeto atualizado é retornado.

---

#### `Update(int id, Product updated)`

```csharp
public bool Update(int id, Product updated)
{
    var product = GetById(id);
    if (product is null) return false;

    product.Name = updated.Name;
    product.Price = updated.Price;
    product.Quantity = updated.Quantity;

    _context.SaveChanges();
    return true;
}
```

Busca o produto existente e atualiza suas propriedades diretamente. Como o objeto foi obtido pelo `GetById()`, ele já está sendo rastreado pelo EF Core. Ao chamar `.SaveChanges()`, o EF Core detecta as mudanças e gera automaticamente o `UPDATE` apenas com as colunas modificadas. Retorna `false` se o produto não for encontrado.

---

#### `Delete(int id)`

```csharp
public bool Delete(int id)
{
    var product = GetById(id);
    if (product is null) return false;

    _context.Products.Remove(product);
    _context.SaveChanges();
    return true;
}
```

Marca o produto para remoção com `.Remove()` e executa o `DELETE FROM` no banco com `.SaveChanges()`. Assim como no `Update`, retorna `false` quando o produto não existe, permitindo que o Controller responda com o status HTTP adequado.

---

### 6. Controller — `Controller/ProductController.cs`

O **Controller** é o ponto de entrada das requisições HTTP. Ele define as rotas da API, recebe os dados da requisição, delega o processamento ao Repository e retorna a resposta HTTP apropriada.

```csharp
using Microsoft.AspNetCore.Mvc;
using NetCrud.DTO;
using NetCrud.Model;
using NetCrud.Repositories;

[ApiController]
[Route("api/[controller]")]
public class ProductController : ControllerBase
{
    private ProductRepository _repository;

    public ProductController(ProductRepository repository)
    {
        _repository = repository;
    }

    // ... métodos abaixo
}
```

- `[ApiController]` — Habilita comportamentos automáticos como validação de model state e inferência de `[FromBody]`.
- `[Route("api/[controller]")]` — Define o prefixo da rota. O token `[controller]` é substituído pelo nome da classe sem o sufixo "Controller", resultando em `api/product`.
- O `ProductRepository` é recebido via injeção de dependência no construtor.

---

#### `GET /api/product/GetAll`

```csharp
[HttpGet("GetAll")]
public ActionResult<IEnumerable<Product>> GetAll() => Ok(_repository.GetAll());
```

Chama `GetAll()` no repositório e retorna a lista completa de produtos com o status `200 OK`. O `ActionResult<T>` permite retornar tanto o dado tipado quanto respostas de erro HTTP.

---

#### `GET /api/product/GetById/{id}`

```csharp
[HttpGet("GetById/{id}")]
public ActionResult<Product> GetById(int id)
{
    var product = _repository.GetById(id);
    return product is null ? NotFound($"Produto com ID {id} não encontrado.") : Ok(product);
}
```

Busca um produto pelo `id` informado na URL. Retorna `200 OK` com o produto caso encontrado, ou `404 Not Found` com uma mensagem descritiva caso não exista.

---

#### `POST /api/product/Create`

```csharp
[HttpPost("Create/{id}")]
public ActionResult<Product> Create([FromBody] ProductDto dto)
{
    var created = _repository.Create(new Product { Name = dto.Name, Price = dto.Price, Quantity = dto.Quantity });
    return CreatedAtAction(nameof(GetById), new { id = created.Id }, created);
}
```

Recebe um `ProductDto` no corpo da requisição (`[FromBody]`), constrói um objeto `Product` a partir dele e o persiste via repositório. Retorna `201 Created` com o cabeçalho `Location` apontando para a rota `GetById` do novo recurso — seguindo a convenção REST para criação de recursos.

---

#### `PUT /api/product/Update/{id}`

```csharp
[HttpPut("Update/{id}")]
public ActionResult Update(int id, [FromBody] ProductDto dto)
{
    var updated = new Product { Name = dto.Name, Price = dto.Price, Quantity = dto.Quantity };
    return _repository.Update(id, updated) ? NoContent() : NotFound($"Produto com ID {id} não encontrado.");
}
```

Recebe o `id` pela URL e os novos dados pelo corpo da requisição. Delega a atualização ao repositório e retorna `204 No Content` em caso de sucesso (sem corpo na resposta, conforme a semântica HTTP do PUT), ou `404 Not Found` se o produto não existir.

---

#### `DELETE /api/product/Delete/{id}`

```csharp
[HttpDelete("Delete/{id}")]
public ActionResult Delete(int id) =>
    _repository.Delete(id) ? NoContent() : NotFound($"Produto com ID {id} não encontrado.");
```

Recebe o `id` pela URL e solicita a exclusão ao repositório. Retorna `204 No Content` em caso de sucesso, ou `404 Not Found` se o produto não for localizado.

---

### 7. Configuração — `Program.cs`

O `Program.cs` é o ponto de entrada da aplicação. É aqui que todos os serviços são registrados no contêiner de injeção de dependência e o pipeline HTTP é configurado.

```csharp
using Microsoft.EntityFrameworkCore;
using NetCrud.Data;
using NetCrud.Repositories;
using Scalar.AspNetCore;

var builder = WebApplication.CreateBuilder(args);

builder.Services.AddDbContext<AppDbContext>(options =>
    options.UseSqlite("Data Source=products.db"));

builder.Services.AddScoped<ProductRepository>();

builder.Services.AddOpenApi();
builder.Services.AddControllers();

var app = builder.Build();

using (var scope = app.Services.CreateScope())
{
    var db = scope.ServiceProvider.GetRequiredService<AppDbContext>();
    db.Database.EnsureCreated();

    if (!db.Products.Any())
    {
        var sql = File.ReadAllText("Data/import.sql");
        db.Database.ExecuteSqlRaw(sql);
        Console.WriteLine("[Import] Dados do import.sql inseridos com sucesso.");
    }
}

if (app.Environment.IsDevelopment())
{
    app.MapOpenApi();
    app.MapScalarApiReference();
}

app.UseHttpsRedirection();
app.MapControllers();

app.Run();
```

**Como funciona:**

- `AddDbContext<AppDbContext>` — Registra o contexto do EF Core com o SQLite. O arquivo `products.db` será criado automaticamente na raiz do projeto.
- `AddScoped<ProductRepository>` — Registra o repositório com o ciclo de vida **Scoped**, ou seja, uma nova instância é criada por requisição HTTP e descartada ao final dela.
- `EnsureCreated()` — Verifica se o banco de dados e a tabela `Products` já existem; se não, os cria automaticamente com base no `AppDbContext`.
- O bloco `if (!db.Products.Any())` — Executa o `import.sql` apenas se a tabela estiver vazia, evitando duplicação de dados em reinicializações do servidor.
- `MapScalarApiReference()` — Disponibiliza a interface de documentação interativa em `/scalar/v1`.

---

## 💡 Conclusão

O uso do framework .NET, especialmente na versão 9, demonstra-se extremamente eficiente para o desenvolvimento de APIs robustas.

**Facilidade de Configuração:** A configuração é simplificada pelo uso de Minimal APIs e injeção de dependência nativa. A integração com o SQLite permite que o desenvolvedor comece a trabalhar imediatamente sem a necessidade de instalar servidores de banco de dados complexos.

**Qualidade do Material:** O material disponível na documentação oficial (Microsoft Learn) é de altíssima qualidade, sendo um dos ecossistemas mais bem documentados do mundo. A comunidade é vasta, o que facilita a resolução de problemas através de fóruns como StackOverflow e GitHub.