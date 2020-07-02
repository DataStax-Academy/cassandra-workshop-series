using System;
using Newtonsoft.Json;

namespace getting_started_with_astra_csharp.Models
{
    /// <summary>
    /// Represents a row in the spacecraft_journey_catalog table
    /// </summary>
    public class spacecraft_journey_catalog
    {
        [JsonProperty(PropertyName = "spacecraft_name")]
        public string Spacecraft_Name { get; set; }
        [JsonProperty(PropertyName = "journey_id")]
        public Guid Journey_Id { get; set; }
        public DateTimeOffset Start { get; set; }
        public DateTimeOffset End { get; set; }
        public string Summary { get; set; }
        public bool Active { get; set; }
    }

}