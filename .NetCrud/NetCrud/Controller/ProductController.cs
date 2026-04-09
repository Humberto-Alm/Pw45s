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

    [HttpGet("GetAll")]
    public ActionResult<IEnumerable<Product>> GetAll() =>Ok(_repository.GetAll());

    [HttpGet("GetById/{id}")]
    public ActionResult<Product> GetById(int id)
    {
        var produto = _repository.GetById(id);
        return produto is null ? NotFound($"Produto com ID {id} não encontrado.") : Ok(produto);
    }

    [HttpPost("Create/{id}")]
    public ActionResult<Product> Create([FromBody] ProductDto dto)
    {
        var criado = _repository.Create(new Product { Nome = dto.Nome, Preco = dto.Preco, Quantidade = dto.Quantidade });
        return CreatedAtAction(nameof(GetById), new { id = criado.Id }, criado);
    }

    [HttpPut("Update/{id}")]
    public ActionResult Update(int id, [FromBody] ProductDto dto)
    {
        var atualizado = new Product { Nome = dto.Nome, Preco = dto.Preco, Quantidade = dto.Quantidade };
        return _repository.Update(id, atualizado) ? NoContent() : NotFound($"Produto com ID {id} não encontrado.");
    }

    [HttpDelete("Delete/{id}")]
    public ActionResult Delete(int id) =>
        _repository.Delete(id) ? NoContent() : NotFound($"Produto com ID {id} não encontrado.");
}