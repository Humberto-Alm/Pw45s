using NetCrud.Model;

namespace NetCrud.Repositories;

public class ProductRepository
{
    private static int _nextId = 2;
    private static List<Product> _products = new()
    {
        new Product { Id = 1, Name = "Teclado", Price = 199.99M, Quantity = 5 }
    };

    public IEnumerable<Product> GetAll() => _products;

    public Product? GetById(int id) => _products.FirstOrDefault(p => p.Id == id);

    public Product Create(Product product)
    {
        product.Id = _nextId++;
        _products.Add(product);
        return product;
    }

    public bool Update(int id, Product updated)
    {
        var product = GetById(id);
        if (product is null) 
            return false;

        product.Name = updated.Name;
        product.Price = updated.Price;
        product.Quantity = updated.Quantity;

        return true;
    }

    public bool Delete(int id)
    {
        var product = GetById(id);
        if (product is null) 
            return false;

        _products.Remove(product);
        return true;
    }
}