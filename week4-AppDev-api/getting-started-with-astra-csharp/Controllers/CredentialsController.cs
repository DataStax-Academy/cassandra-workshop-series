using getting_started_with_astra_csharp.Interfaces;
using Microsoft.AspNetCore.Mvc;
using System;
using System.IO;
using Microsoft.AspNetCore.Http;
using System.Threading.Tasks;

namespace getting_started_with_astra_csharp.Controllers
{
    /// <summary>
    /// Works with the managing the credentials for the Astra database
    /// </summary>
    [Route("api/[controller]")]
    [ApiController]
    public class CredentialsController : ControllerBase
    {
        private IDataStaxService Service { get; set; }

        public CredentialsController(IDataStaxService service)
        {
            Service = service;
        }

        /// <summary>
        /// This checks to see if there is an existing connection to the Astra database
        /// </summary>
        /// <returns>A 200 if a valid connection exists, a 401 if one does not</returns>
        [ProducesResponseType(200)]     // OK
        [ProducesResponseType(401)]     // Unauthorized
        [HttpGet]
        public IActionResult CheckConnection()
        {
            try
            {
                if (Service.Session != null)
                {
                    return Ok();
                }
                else
                {
                    return Unauthorized();
                };
            }
            catch (Exception ex)
            {
                var res = new JsonResult(ex.Message);
                res.StatusCode = StatusCodes.Status401Unauthorized;
                return res;
            }
        }

        // GET api/credentials/test
        /// <summary>
        /// This tests the provided parameters and checks to see if they establish a valid connection to the Astra database
        /// </summary>
        /// <param name="username">The Astra database user name</param>
        /// <param name="password">The Astra database password for the user name</param>
        /// <param name="keyspace">The keyspace in the Astra database</param>
        /// <param name="file">A body element containing the secure connect bundle file</param>
        /// <returns>A 200 if the test was successful, a 401 if it was not</returns>
        [ProducesResponseType(200)]     // OK
        [ProducesResponseType(401)]     // Unauthorized
        [HttpPost("test")]
        public async Task<IActionResult> TestCredentials([FromQuery]string username, [FromQuery]string password, [FromQuery]string keyspace, IFormFile file)
        {
            //Copy the secure connect bundle to a temporary locationobj
            var filePath = Path.GetTempPath() + "/" + Guid.NewGuid() + ".zip";
            var output = System.IO.File.OpenWrite(filePath);
            file.CopyTo(output);
            output.Close();

            //Now test to see if it works
            var result = await Service.TestConnection(username, password, keyspace, filePath);
            if (result.Item1)
            {
                return Ok();
            }
            else
            {
                var res = new JsonResult(result.Item2);
                res.StatusCode = StatusCodes.Status401Unauthorized;
                return res;
            }
        }

        // GET api/credentials
        /// <summary>
        /// This tests and then saves the provided parameters and checks to see if they establish a valid connection to the Astra database
        /// </summary>
        /// <param name="username">The Astra database user name</param>
        /// <param name="password">The Astra database password for the user name</param>
        /// <param name="keyspace">The keyspace in the Astra database</param>
        /// <param name="file">A body element containing the secure connect bundle file</param>
        /// <returns>A 200 if a valid connection exists, a 401 if one does not</returns>
        [ProducesResponseType(200)]     // OK
        [ProducesResponseType(401)]     // Unauthorized
        [HttpPost]
        public async Task<IActionResult> SaveCredentials([FromQuery]string username, [FromQuery]string password, [FromQuery]string keyspace, IFormFile file)
        {
            //Copy the secure connect bundle to a temporary location
            var filePath = Path.GetTempPath() + "/" + Guid.NewGuid() + ".zip";
            var output = System.IO.File.OpenWrite(filePath);
            file.CopyTo(output);
            output.Close();

            //Now test to see if it works
            var result = await Service.SaveConnection(username, password, keyspace, filePath);
            if (result.Item1)
            {
                return Ok();
            }
            else
            {
                var res = new JsonResult(result.Item2);
                res.StatusCode = StatusCodes.Status401Unauthorized;
                return res;
            }
        }
    }
}
