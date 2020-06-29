using System;
using Newtonsoft.Json;

namespace getting_started_with_astra_csharp.Models
{
    /// <summary>
    /// This class provides a wrapper around a result set which contains the data results as well as the returned page size and page state.
    /// </summary>
    /// <typeparam name="T">The type of data treturned</typeparam>
    public class PagedResultWrapper<T>
    {
        public PagedResultWrapper()
        {
        }

        public PagedResultWrapper(int pageSize, byte[] pageState, T data)
        {
            PageSize = pageSize;
            PageState = pageState != null ? Convert.ToBase64String(pageState) : null;
            Data = data;
        }

        public int PageSize { get; set; }
        public string PageState { get; set; }
        public T Data { get; set; }
    }

}