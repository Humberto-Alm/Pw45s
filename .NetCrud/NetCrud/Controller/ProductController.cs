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
        var product = _repository.GetById(id);
        return product is null ? NotFound($"Produto com ID {id} não encontrado.") : Ok(product);
    }

    [HttpPost("Create/{id}")]
    public ActionResult<Product> Create([FromBody] ProductDto dto)
    {
        var created = _repository.Create(new Product { Name = dto.Name, Price = dto.Price, Quantity = dto.Quantity });
        return CreatedAtAction(nameof(GetById), new { id = created.Id }, created);
    }

    [HttpPut("Update/{id}")]
    public ActionResult Update(int id, [FromBody] ProductDto dto)
    {
        var updated = new Product { Name = dto.Name, Price = dto.Price, Quantity = dto.Quantity };
        return _repository.Update(id, updated) ? NoContent() : NotFound($"Produto com ID {id} não encontrado.");
    }

    [HttpDelete("Delete/{id}")]
    public ActionResult Delete(int id) =>
        _repository.Delete(id) ? NoContent() : NotFound($"Produto com ID {id} não encontrado.");
}