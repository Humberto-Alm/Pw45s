using Microsoft.AspNetCore.Mvc;
using NetCrud.Model;

namespace NetCrud.Controllers;


[ApiController]
[Route("api/[controller]")]
public class ProductController : ControllerBase
{
    // 💾 Armazenamento em memória (estático para persistir entre requisições)
    private static List<Product> _produtos = new();
    private static int _proximoId = 1;

    // ✅ READ ALL - GET /api/produtos
    [HttpGet]
    public ActionResult<IEnumerable<Product>> GetAll()
    {
        return Ok(_produtos);
    }

    // ✅ READ ONE - GET /api/produtos/{id}
    [HttpGet("{id}")]
    public ActionResult<Product> GetById(int id)
    {
        var produto = _produtos.FirstOrDefault(p => p.Id == id);
        if (produto is null)
            return NotFound($"Produto com ID {id} não encontrado.");

        return Ok(produto);
    }

    // ✅ CREATE - POST /api/produtos
    [HttpPost]
    public ActionResult<Product> Create([FromBody] Product novoProduto)
    {
        novoProduto.Id = _proximoId++;
        _produtos.Add(novoProduto);

        return CreatedAtAction(nameof(GetById), new { id = novoProduto.Id }, novoProduto);
    }

    // ✅ UPDATE - PUT /api/produtos/{id}
    [HttpPut("{id}")]
    public ActionResult Update(int id, [FromBody] Product produtoAtualizado)
    {
        var produto = _produtos.FirstOrDefault(p => p.Id == id);
        if (produto is null)
            return NotFound($"Produto com ID {id} não encontrado.");

        produto.Nome = produtoAtualizado.Nome;
        produto.Preco = produtoAtualizado.Preco;
        produto.Quantidade = produtoAtualizado.Quantidade;

        return NoContent();
    }

    // ✅ DELETE - DELETE /api/produtos/{id}
    [HttpDelete("{id}")]
    public ActionResult Delete(int id)
    {
        var produto = _produtos.FirstOrDefault(p => p.Id == id);
        if (produto is null)
            return NotFound($"Produto com ID {id} não encontrado.");

        _produtos.Remove(produto);
        return NoContent();
    } 
}

