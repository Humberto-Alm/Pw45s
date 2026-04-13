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
}

if (app.Environment.IsDevelopment())
{
    app.MapOpenApi();
    app.MapScalarApiReference();
}

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

app.UseHttpsRedirection();
app.MapControllers();

app.Run();