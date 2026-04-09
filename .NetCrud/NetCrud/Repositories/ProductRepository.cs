using NetCrud.Model;

namespace NetCrud.Repositories;

public class ProductRepository
{
    private static int _proximoId = 2;
    private static List<Product> _produtos = new()
    {
        new Product { Id = 1, Nome = "Teclado", Preco = 199.99M, Quantidade = 5 }
    };

    public IEnumerable<Product> GetAll() => _produtos;

    public Product? GetById(int id) => _produtos.FirstOrDefault(p => p.Id == id);

    public Product Create(Product product)
    {
        product.Id = _proximoId++;
        _produtos.Add(product);
        return product;
    }

    public bool Update(int id, Product atualizado)
    {
        var produto = GetById(id);
        if (produto is null) 
            return false;

        produto.Nome = atualizado.Nome;
        produto.Preco = atualizado.Preco;
        produto.Quantidade = atualizado.Quantidade;

        return true;
    }

    public bool Delete(int id)
    {
        var produto = GetById(id);
        if (produto is null) 
            return false;

        _produtos.Remove(produto);
        return true;
    }
}