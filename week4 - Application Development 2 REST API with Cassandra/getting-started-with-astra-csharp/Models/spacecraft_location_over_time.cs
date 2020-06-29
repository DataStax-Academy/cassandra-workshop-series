using System;
using Newtonsoft.Json;

namespace getting_started_with_astra_csharp.Models
{
    /// <summary>
    /// Represents a row in the spacecraft_location_over_time table
    /// </summary>
    public class spacecraft_location_over_time
    {
        [JsonProperty(PropertyName = "spacecraft_name")]
        public string Spacecraft_Name { get; set; }
        [JsonProperty(PropertyName = "journey_id")]
        public Guid Journey_Id { get; set; }
        public location_udt Location { get; set; }
        [JsonProperty(PropertyName = "reading_time")]
        public DateTimeOffset Reading_Time { get; set; }
        [JsonProperty(PropertyName = "location_unit")]
        public string Location_Unit { get; set; }
    }

}