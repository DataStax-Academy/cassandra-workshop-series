using System.Collections.Generic;
using System.Linq;
using getting_started_with_astra_csharp.Interfaces;
using Microsoft.AspNetCore.Mvc;
using Cassandra.Data.Linq;
using Cassandra.Mapping;
using System;
using System.Threading.Tasks;

namespace getting_started_with_astra_csharp.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class SpacecraftController : ControllerBase
    {
        private IDataStaxService Service { get; set; }

        public SpacecraftController(IDataStaxService service)
        {
            Service = service;
        }

        // GET api/spacecrafts
        /// <summary>
        /// This returns all spacecraft in the database
        /// </summary>
        /// <returns>An array of spacecraft_journey_catalog objects</returns>
        [HttpGet]
        public async Task<ActionResult<ICollection<Models.spacecraft_journey_catalog>>> GetAllSpaceCraft()
        {
            var spaceCraft = new Table<Models.spacecraft_journey_catalog>(Service.Session);
            var crafts = await spaceCraft.ExecuteAsync();
            return crafts.OrderBy(s => s.Spacecraft_Name).ThenBy(s => s.Start).ToList();
        }

        // GET api/spacecrafts/{spaceCraftName}
        /// <summary>
        /// This returns all journeys for a specific spacecraft
        /// </summary>
        /// <param name="spaceCraftName">The spacecraft to return data for</param>
        /// <returns>An array of spacecraft_journey_catalog objects</returns>
        [HttpGet("{spaceCraftName}")]
        public async Task<ICollection<Models.spacecraft_journey_catalog>> GetJourneysForSpacecraft(string spaceCraftName)
        {
            var spaceCraft = new Table<Models.spacecraft_journey_catalog>(Service.Session);
            var craft = await spaceCraft.Where(s => s.Spacecraft_Name == spaceCraftName).ExecuteAsync();
            return craft.OrderBy(s => s.Start).ToList();
        }

        // POST api/spacecrafts/{spaceCraftName}
        /// <summary>
        /// Create a new journey for the specified spacecraft with the specified summary
        /// </summary>
        /// <param name="spaceCraftName">The spacecraft name</param>
        /// <param name="summary">The summary to associate with the spacecraft</param>
        /// <returns>The newly created journey id</returns>
        [HttpPost("{spaceCraftName}")]
        public async Task<Guid> CreateJourneyForSpacecraft(string spaceCraftName, [FromBody]string summary)
        {
            IMapper mapper = new Mapper(Service.Session);
            var journey = new Models.spacecraft_journey_catalog();
            journey.Spacecraft_Name = spaceCraftName;
            journey.Journey_Id = Cassandra.TimeUuid.NewId();
            journey.Active = false;
            journey.Start = DateTimeOffset.Now;
            journey.End = DateTimeOffset.Now.AddSeconds(1000);
            journey.Summary = summary;

            await mapper.InsertAsync(journey);

            return journey.Journey_Id;
        }
    }
}
