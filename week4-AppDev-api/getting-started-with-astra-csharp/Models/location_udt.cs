using System;
using Newtonsoft.Json;

namespace getting_started_with_astra_csharp.Models
{
    /// <summary>
    /// A C# representation of the location udt in the Astra database
    /// </summary>
    public class location_udt
    {
        [JsonProperty(PropertyName = "x_coordinate")]
        public double X_Coordinate { get; set; }
        [JsonProperty(PropertyName = "y_coordinate")]
        public double Y_Coordinate { get; set; }
        [JsonProperty(PropertyName = "z_coordinate")]
        public double Z_Coordinate { get; set; }
    }

}
