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

    public IEnumerable<Product> GetAll() => _context.Products.ToList();

    public Product? GetById(int id) => _context.Products.Find(id);

    public Product Create(Product product)
    {
        _context.Products.Add(product);
        _context.SaveChanges();
        return product;
    }

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

    public bool Delete(int id)
    {
        var product = GetById(id);
        if (product is null) return false;

        _context.Products.Remove(product);
        _context.SaveChanges();
        return true;
    }
}