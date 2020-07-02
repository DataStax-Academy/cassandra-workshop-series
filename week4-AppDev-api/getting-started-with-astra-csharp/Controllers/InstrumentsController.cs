using System;
using System.Collections.Generic;
using System.Linq;
using getting_started_with_astra_csharp.Interfaces;
using Microsoft.AspNetCore.Mvc;
using getting_started_with_astra_csharp.Models;
using Cassandra.Data.Linq;
using Cassandra.Mapping;
using System.Threading.Tasks;

namespace getting_started_with_astra_csharp.Controllers
{
    /// <summary>
    /// Handles operations around the instrument readings of a spacecraft journey
    /// </summary>
    [Route("api/spacecraft/{spaceCraftName}/{journeyId}/instruments")]
    [ApiController]
    public class InstrumentsController : ControllerBase
    {
        private IDataStaxService Service { get; set; }

        public InstrumentsController(IDataStaxService service)
        {
            Service = service;
        }

        // GET api/spacecraft/{spaceCraftName}/{journeyId}/instruments/temperature
        /// <summary>
        /// This returns the temperature readings for specified page and page size for the spacecraft and journey.
        /// </summary>
        /// <param name="spaceCraftName">The name of the spacecraft</param>
        /// <param name="journeyId">The id of the journey</param>
        /// <param name="pageState">The pagestate of a previous request to continue, or null if this is a new request</param>
        /// <param name="pageSize">The page size to return</param>
        /// <returns>A PageResultWrapper containing the results</returns>
        [HttpGet("temperature")]
        public async Task<PagedResultWrapper<ICollection<spacecraft_temperature_over_time>>> GetTemperatureReading(string spaceCraftName, Guid journeyId,
            [FromQuery]string pageState, [FromQuery]int? pageSize)
        {
            var spaceCraft = new Table<spacecraft_temperature_over_time>(Service.Session);
            var query = spaceCraft.
                Where(s => s.Spacecraft_Name == spaceCraftName && s.Journey_Id == journeyId);
            if (pageSize.HasValue)
            {
                query.SetPageSize(pageSize.Value);
            }
            if (pageState != null && pageState.Length > 0)
            {
                query.SetPagingState(Convert.FromBase64String(pageState));
            }
            var temperature = await query.ExecutePagedAsync();
            return new PagedResultWrapper<ICollection<spacecraft_temperature_over_time>>(pageSize.HasValue ? pageSize.Value : 0,
                temperature.PagingState, temperature.OrderBy(s => s.Reading_Time).ToList()
            );
        }

        // GET api/spacecraft/{spaceCraftName}/{journeyId}/instruments/pressure
        /// <summary>
        /// This returns the pressure readings for specified page and page size for the spacecraft and journey.
        /// </summary>
        /// <param name="spaceCraftName">The name of the spacecraft</param>
        /// <param name="journeyId">The id of the journey</param>
        /// <param name="pageState">The pagestate of a previous request to continue, or null if this is a new request</param>
        /// <param name="pageSize">The page size to return</param>
        /// <returns>A PageResultWrapper containing the results</returns>
        [HttpGet("pressure")]
        public async Task<ActionResult<PagedResultWrapper<ICollection<spacecraft_pressure_over_time>>>> GetPressureReading(string spaceCraftName, Guid journeyId,
                    [FromQuery]string pageState, [FromQuery]int? pageSize)
        {

            var spaceCraft = new Table<spacecraft_pressure_over_time>(Service.Session);
            var query = spaceCraft.
                Where(s => s.Spacecraft_Name == spaceCraftName && s.Journey_Id == journeyId);
            if (pageSize.HasValue)
            {
                query.SetPageSize(pageSize.Value);
            }
            if (pageState != null && pageState.Length > 0)
            {
                query.SetPagingState(Convert.FromBase64String(pageState));
            }
            var pressure = await query.ExecutePagedAsync();
            return Ok(new PagedResultWrapper<ICollection<spacecraft_pressure_over_time>>(pageSize.HasValue ? pageSize.Value : 0,
                pressure.PagingState, pressure.OrderBy(s => s.Reading_Time).ToList())
            );
        }

        // GET api/spacecraft/{spaceCraftName}/{journeyId}/instruments/location
        /// <summary>
        /// This returns the location readings for specified page and page size for the spacecraft and journey.
        /// </summary>
        /// <param name="spaceCraftName">The name of the spacecraft</param>
        /// <param name="journeyId">The id of the journey</param>
        /// <param name="pageState">The pagestate of a previous request to continue, or null if this is a new request</param>
        /// <param name="pageSize">The page size to return</param>
        /// <returns>A PageResultWrapper containing the results</returns>
        [HttpGet("location")]
        public async Task<ActionResult<PagedResultWrapper<ICollection<spacecraft_location_over_time>>>> GetLocationReading(string spaceCraftName, Guid journeyId,
                    [FromQuery]string pageState, [FromQuery]int? pageSize)
        {
            var spaceCraft = new Table<spacecraft_location_over_time>(Service.Session);
            var query = spaceCraft.
                Where(s => s.Spacecraft_Name == spaceCraftName && s.Journey_Id == journeyId);
            if (pageSize.HasValue)
            {
                query.SetPageSize(pageSize.Value);
            }
            if (pageState != null && pageState.Length > 0)
            {
                query.SetPagingState(Convert.FromBase64String(pageState));
            }
            var location = await query.ExecutePagedAsync();
            return new PagedResultWrapper<ICollection<spacecraft_location_over_time>>(pageSize.HasValue ? pageSize.Value : 0,
                location.PagingState, location.OrderBy(s => s.Reading_Time).ToList()
            );
        }

        // GET api/spacecraft/{spaceCraftName}/{journeyId}/instruments/speed
        /// <summary>
        /// This returns the speed readings for specified page and page size for the spacecraft and journey.
        /// </summary>
        /// <param name="spaceCraftName">The name of the spacecraft</param>
        /// <param name="journeyId">The id of the journey</param>
        /// <param name="pageState">The pagestate of a previous request to continue, or null if this is a new request</param>
        /// <param name="pageSize">The page size to return</param>
        /// <returns>A PageResultWrapper containing the results</returns>
        [HttpGet("speed")]
        public async Task<ActionResult<PagedResultWrapper<ICollection<spacecraft_speed_over_time>>>> GetSpeedReading(string spaceCraftName, Guid journeyId,
                    [FromQuery]string pageState, [FromQuery]int? pageSize)
        {
            var spaceCraft = new Table<spacecraft_speed_over_time>(Service.Session);
            var query = spaceCraft.
                Where(s => s.Spacecraft_Name == spaceCraftName && s.Journey_Id == journeyId);
            if (pageSize.HasValue)
            {
                query.SetPageSize(pageSize.Value);
            }
            if (pageState != null && pageState.Length > 0)
            {
                query.SetPagingState(Convert.FromBase64String(pageState));
            }
            var speed = await query.ExecutePagedAsync();
            return new PagedResultWrapper<ICollection<spacecraft_speed_over_time>>(pageSize.HasValue ? pageSize.Value : 0,
                speed.PagingState, speed.OrderBy(s => s.Reading_Time).ToList()
            );
        }

        /// <summary>
        /// This accepts an array of spacecraft_temperature_over_time objects and saves them to the database
        /// </summary>
        /// <param name="temperatures">The array of spacecraft_temperature_over_time</param>
        /// <returns>A 200 if successful, other wise an error</returns>
        [HttpPost("temperature")]
        public async Task<IActionResult> SaveTemperatures([FromBody]spacecraft_temperature_over_time[] temperatures)
        {
            IMapper mapper = new Mapper(Service.Session);
            var batch = mapper.CreateBatch();
            for (int i = 0; i < temperatures.Count(); i++)
            {
                batch.Insert(temperatures[i]);
            }
            await mapper.ExecuteAsync(batch);
            return Ok();
        }

        /// <summary>
        /// This accepts an array of spacecraft_pressure_over_time objects and saves them to the database
        /// </summary>
        /// <param name="pressures">The array of spacecraft_pressure_over_time</param>
        /// <returns>A 200 if successful, other wise an error</returns>
        [HttpPost("pressure")]
        public async Task<IActionResult> SavePressures([FromBody]spacecraft_pressure_over_time[] pressures)
        {
            IMapper mapper = new Mapper(Service.Session);
            var batch = mapper.CreateBatch();
            for (int i = 0; i < pressures.Count(); i++)
            {
                batch.Insert(pressures[i]);
            }
            await mapper.ExecuteAsync(batch);
            return Ok();
        }

        /// <summary>
        /// This accepts an array of spacecraft_speed_over_time objects and saves them to the database
        /// </summary>
        /// <param name="speed">The array of spacecraft_speed_over_time</param>
        /// <returns>A 200 if successful, other wise an error</returns>
        [HttpPost("speed")]
        public async Task<IActionResult> SaveSpeed([FromBody]spacecraft_speed_over_time[] speed)
        {
            IMapper mapper = new Mapper(Service.Session);
            var batch = mapper.CreateBatch();
            for (int i = 0; i < speed.Count(); i++)
            {
                batch.Insert(speed[i]);
            }
            await mapper.ExecuteAsync(batch);
            return Ok();
        }

        /// <summary>
        /// This accepts an array of spacecraft_location_over_time objects and saves them to the database
        /// </summary>
        /// <param name="locations">The array of spacecraft_location_over_time</param>
        /// <returns>A 200 if successful, other wise an error</returns>
        [HttpPost("location")]
        public async Task<ActionResult> SaveLocations([FromBody]spacecraft_location_over_time[] locations)
        {
            IMapper mapper = new Mapper(Service.Session);
            var batch = mapper.CreateBatch();
            for (int i = 0; i < locations.Count(); i++)
            {
                batch.Insert(locations[i]);
            }
            await mapper.ExecuteAsync(batch);
            return Ok();
        }
    }
}
