using System.Threading.Tasks;
using Cassandra;

namespace getting_started_with_astra_csharp.Interfaces
{
    public interface IDataStaxService
    {
        ISession Session { get; }

        Task<System.Tuple<bool, string>> SaveConnection(string username, string password, string keyspace, string secureConnectBundlePath);
        Task<System.Tuple<bool, string>> TestConnection(string username, string password, string keyspace, string secureConnectBundlePath);
    }
}