using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.HttpsPolicy;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;
using Microsoft.Extensions.Options;

namespace getting_started_with_astra_csharp
{
    public class Startup
    {
        public Startup(IConfiguration configuration)
        {
            Configuration = configuration;
        }

        public IConfiguration Configuration { get; }

        // This method gets called by the runtime. Use this method to add services to the container.
        public void ConfigureServices(IServiceCollection services)
        {
            //Add Swagger Document Properties
            services.AddSwaggerDocument(config =>
            {
                config.PostProcess = document =>
               {
                   document.Info.Version = "v1";
                   document.Info.Title = "Getting Started with Astra - C# Backend";
                   document.Info.Description = "A simple ASP.NET Core web API version of the Getting Started with Astra backend for use with the Getting Started with Astra UI";
                   document.Info.TermsOfService = "None";
                   document.Info.Contact = new NSwag.OpenApiContact
                   {
                       Name = "Dave Bechberger",
                       Email = string.Empty,
                       Url = "https://github.com/bechbd"
                   };
                   document.Info.License = new NSwag.OpenApiLicense
                   {
                       Name = "Apache 2.0",
                       Url = "https://www.apache.org/licenses/LICENSE-2.0"
                   };
               };
            });

            //Enable Cors
            services.AddCors(o => o.AddPolicy("AllowAllPolicy", builder =>
            {
                builder.AllowAnyOrigin()
                    .AllowAnyMethod()
                    .AllowAnyHeader();
            }));

            //This adds a singleton of the Astra Session connection to dependency injection
            services.AddSingleton(typeof(Interfaces.IDataStaxService), typeof(Services.AstraService));
            services.AddMvc().SetCompatibilityVersion(CompatibilityVersion.Version_2_1);
        }

        // This method gets called by the runtime. Use this method to configure the HTTP request pipeline.
        public void Configure(IApplicationBuilder app, IHostingEnvironment env)
        {
            if (env.IsDevelopment())
            {
                app.UseDeveloperExceptionPage();
            }
            else
            {
                // The default HSTS value is 30 days. You may want to change this for production scenarios, see https://aka.ms/aspnetcore-hsts.
                app.UseHsts();
            }
            //Add Swagger
            app.UseOpenApi();
            app.UseSwaggerUi3();

            //Add CORS Support
            app.UseCors("AllowAllPolicy");
            app.UseMvc();
        }
    }
}
