#pragma checksum "C:\Users\Aykhan\Desktop\CanStellar\YIS\CanStellarBack\CanStellarBack\Views\Home\Index.cshtml" "{ff1816ec-aa5e-4d10-87f7-6f4963833460}" "c8c9ebe591c41462b0a36b3817a41a330f887b13"
// <auto-generated/>
#pragma warning disable 1591
[assembly: global::Microsoft.AspNetCore.Razor.Hosting.RazorCompiledItemAttribute(typeof(AspNetCore.Views_Home_Index), @"mvc.1.0.view", @"/Views/Home/Index.cshtml")]
namespace AspNetCore
{
    #line hidden
    using System;
    using System.Collections.Generic;
    using System.Linq;
    using System.Threading.Tasks;
    using Microsoft.AspNetCore.Mvc;
    using Microsoft.AspNetCore.Mvc.Rendering;
    using Microsoft.AspNetCore.Mvc.ViewFeatures;
#nullable restore
#line 26 "C:\Users\Aykhan\Desktop\CanStellar\YIS\CanStellarBack\CanStellarBack\Views\Home\Index.cshtml"
using CanStellarBack.Models;

#line default
#line hidden
#nullable disable
    [global::Microsoft.AspNetCore.Razor.Hosting.RazorSourceChecksumAttribute(@"SHA1", @"c8c9ebe591c41462b0a36b3817a41a330f887b13", @"/Views/Home/Index.cshtml")]
    public class Views_Home_Index : global::Microsoft.AspNetCore.Mvc.Razor.RazorPage<List<Telemetry>>
    {
        #line hidden
        #pragma warning disable 0649
        private global::Microsoft.AspNetCore.Razor.Runtime.TagHelpers.TagHelperExecutionContext __tagHelperExecutionContext;
        #pragma warning restore 0649
        private global::Microsoft.AspNetCore.Razor.Runtime.TagHelpers.TagHelperRunner __tagHelperRunner = new global::Microsoft.AspNetCore.Razor.Runtime.TagHelpers.TagHelperRunner();
        #pragma warning disable 0169
        private string __tagHelperStringValueBuffer;
        #pragma warning restore 0169
        private global::Microsoft.AspNetCore.Razor.Runtime.TagHelpers.TagHelperScopeManager __backed__tagHelperScopeManager = null;
        private global::Microsoft.AspNetCore.Razor.Runtime.TagHelpers.TagHelperScopeManager __tagHelperScopeManager
        {
            get
            {
                if (__backed__tagHelperScopeManager == null)
                {
                    __backed__tagHelperScopeManager = new global::Microsoft.AspNetCore.Razor.Runtime.TagHelpers.TagHelperScopeManager(StartTagHelperWritingScope, EndTagHelperWritingScope);
                }
                return __backed__tagHelperScopeManager;
            }
        }
        private global::Microsoft.AspNetCore.Mvc.Razor.TagHelpers.HeadTagHelper __Microsoft_AspNetCore_Mvc_Razor_TagHelpers_HeadTagHelper;
        private global::Microsoft.AspNetCore.Mvc.Razor.TagHelpers.BodyTagHelper __Microsoft_AspNetCore_Mvc_Razor_TagHelpers_BodyTagHelper;
        #pragma warning disable 1998
        public async override global::System.Threading.Tasks.Task ExecuteAsync()
        {
            DefineSection("scripts", async() => {
                WriteLiteral("\r\n    <script src=\"https://code.jquery.com/jquery-3.6.0.min.js\"></script>\r\n    <script>\r\n        $(document).ready(function () {\r\n            setInterval(function () {\r\n                $.ajax({\r\n                    url: \'");
#nullable restore
#line 12 "C:\Users\Aykhan\Desktop\CanStellar\YIS\CanStellarBack\CanStellarBack\Views\Home\Index.cshtml"
                     Write(Url.Action("Index", "HomeController"));

#line default
#line hidden
#nullable disable
                WriteLiteral(@"',
                    type: 'GET',
                    success: function (response) {
                        $('#tele-data').html(response); // Update the data card with the updated data
                    },
                    error: function () {
                        console.log('Error occurred while fetching data.');
                    }
                });
            }, 5000); // Fetch data every 5 seconds (adjust the interval as per your needs)
        });
    </script>
");
            }
            );
            WriteLiteral("\r\n");
            WriteLiteral("<!DOCTYPE html>\r\n<html lang=\"en\">\r\n");
            __tagHelperExecutionContext = __tagHelperScopeManager.Begin("head", global::Microsoft.AspNetCore.Razor.TagHelpers.TagMode.StartTagAndEndTag, "c8c9ebe591c41462b0a36b3817a41a330f887b134111", async() => {
                WriteLiteral(@"
    <meta charset=""UTF-8"">
    <meta http-equiv=""X-UA-Compatible"" content=""IE=edge"">
    <meta name=""viewport"" content=""width=device-width, initial-scale=1.0"">
    <link rel=""stylesheet"" href=""./index.css"">
    <link href=""https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css"" rel=""stylesheet"" integrity=""sha384-KK94CHFLLe+nY2dmCWGMq91rCGa5gtU4mk92HdvYe+M/SXH301p5ILy+dN9+nJOZ"" crossorigin=""anonymous"">
    <title>YİS İnterfeys</title>
");
            }
            );
            __Microsoft_AspNetCore_Mvc_Razor_TagHelpers_HeadTagHelper = CreateTagHelper<global::Microsoft.AspNetCore.Mvc.Razor.TagHelpers.HeadTagHelper>();
            __tagHelperExecutionContext.Add(__Microsoft_AspNetCore_Mvc_Razor_TagHelpers_HeadTagHelper);
            await __tagHelperRunner.RunAsync(__tagHelperExecutionContext);
            if (!__tagHelperExecutionContext.Output.IsContentModified)
            {
                await __tagHelperExecutionContext.SetOutputContentAsync();
            }
            Write(__tagHelperExecutionContext.Output);
            __tagHelperExecutionContext = __tagHelperScopeManager.End();
            WriteLiteral("\r\n");
            __tagHelperExecutionContext = __tagHelperScopeManager.Begin("body", global::Microsoft.AspNetCore.Razor.TagHelpers.TagMode.StartTagAndEndTag, "c8c9ebe591c41462b0a36b3817a41a330f887b135566", async() => {
                WriteLiteral("\r\n    <div id=\"header\" class=\"container-fluid\">\r\n        <img id=\"logo\" src=\"./assets/logo.png\"");
                BeginWriteAttribute("alt", " alt=\"", 1588, "\"", 1594, 0);
                EndWriteAttribute();
                WriteLiteral(">\r\n        <h2>CanStellar YİS İnterfeysi</h2>\r\n        <divs>\r\n    </div>\r\n\r\n    </div>\r\n    <div id=\"main\" class=\"container-fluid\">\r\n        <div id=\"databox\" class=\"container-sm  display-flex flex-direction-column\">\r\n");
#nullable restore
#line 48 "C:\Users\Aykhan\Desktop\CanStellar\YIS\CanStellarBack\CanStellarBack\Views\Home\Index.cshtml"
              
                int s = 1;
                foreach (var item in Model)
                {


#line default
#line hidden
#nullable disable
                WriteLiteral("                        <div id=\"tele-data\">\r\n                            <p id=\"raw-telemetric-data\">");
#nullable restore
#line 54 "C:\Users\Aykhan\Desktop\CanStellar\YIS\CanStellarBack\CanStellarBack\Views\Home\Index.cshtml"
                                                    Write(s);

#line default
#line hidden
#nullable disable
                WriteLiteral(". Xam Telemetriya: ");
#nullable restore
#line 54 "C:\Users\Aykhan\Desktop\CanStellar\YIS\CanStellarBack\CanStellarBack\Views\Home\Index.cshtml"
                                                                          Write(item.Raw);

#line default
#line hidden
#nullable disable
                WriteLiteral("</p>\r\n                            <h6>Detallı telemetriya:</h6>\r\n                            <ol id=\"telemetric-data-list\">\r\n                                <li><p>Komanda ID: ");
#nullable restore
#line 57 "C:\Users\Aykhan\Desktop\CanStellar\YIS\CanStellarBack\CanStellarBack\Views\Home\Index.cshtml"
                                              Write(item.TeamId);

#line default
#line hidden
#nullable disable
                WriteLiteral(",</p></li>\r\n                                <li><p>Çalışma müddəti: ");
#nullable restore
#line 58 "C:\Users\Aykhan\Desktop\CanStellar\YIS\CanStellarBack\CanStellarBack\Views\Home\Index.cshtml"
                                                   Write(item.WorkingTime);

#line default
#line hidden
#nullable disable
                WriteLiteral(" s</p></li>\r\n                                <li><p>Telemetriya paketlərinin sayı: ");
#nullable restore
#line 59 "C:\Users\Aykhan\Desktop\CanStellar\YIS\CanStellarBack\CanStellarBack\Views\Home\Index.cshtml"
                                                                 Write(item.NumberOfPackages);

#line default
#line hidden
#nullable disable
                WriteLiteral("</p></li>\r\n                                <li><p>Batereyanın gərginliyi: ");
#nullable restore
#line 60 "C:\Users\Aykhan\Desktop\CanStellar\YIS\CanStellarBack\CanStellarBack\Views\Home\Index.cshtml"
                                                          Write(item.CurrentVoltage);

#line default
#line hidden
#nullable disable
                WriteLiteral(" V</p></li>\r\n                                <li><p>Hündürlük: ");
#nullable restore
#line 61 "C:\Users\Aykhan\Desktop\CanStellar\YIS\CanStellarBack\CanStellarBack\Views\Home\Index.cshtml"
                                             Write(item.Altitude);

#line default
#line hidden
#nullable disable
                WriteLiteral(" m</p></li>\r\n                                <li><p>Sürət: ");
#nullable restore
#line 62 "C:\Users\Aykhan\Desktop\CanStellar\YIS\CanStellarBack\CanStellarBack\Views\Home\Index.cshtml"
                                         Write(item.Speed);

#line default
#line hidden
#nullable disable
                WriteLiteral(" m/s</p></li>\r\n                                <li><p>Coğrafi en: ");
#nullable restore
#line 63 "C:\Users\Aykhan\Desktop\CanStellar\YIS\CanStellarBack\CanStellarBack\Views\Home\Index.cshtml"
                                              Write(item.Latitude);

#line default
#line hidden
#nullable disable
                WriteLiteral(" </p></li>\r\n                                <li><p>Coğrafi uzunluq: ");
#nullable restore
#line 64 "C:\Users\Aykhan\Desktop\CanStellar\YIS\CanStellarBack\CanStellarBack\Views\Home\Index.cshtml"
                                                   Write(item.Longitude);

#line default
#line hidden
#nullable disable
                WriteLiteral(" </p></li>\r\n                                <li><p>Daşıyıcıdan ayrıldığı vaxt: ");
#nullable restore
#line 65 "C:\Users\Aykhan\Desktop\CanStellar\YIS\CanStellarBack\CanStellarBack\Views\Home\Index.cshtml"
                                                              Write(item.TimeSinceSeperation.Hour);

#line default
#line hidden
#nullable disable
                WriteLiteral(":");
#nullable restore
#line 65 "C:\Users\Aykhan\Desktop\CanStellar\YIS\CanStellarBack\CanStellarBack\Views\Home\Index.cshtml"
                                                                                             Write(item.TimeSinceSeperation.Minute);

#line default
#line hidden
#nullable disable
                WriteLiteral(":");
#nullable restore
#line 65 "C:\Users\Aykhan\Desktop\CanStellar\YIS\CanStellarBack\CanStellarBack\Views\Home\Index.cshtml"
                                                                                                                              Write(item.TimeSinceSeperation.Second);

#line default
#line hidden
#nullable disable
                WriteLiteral("</p></li>\r\n                                <li><p>Videogörüntünün müddəti: ");
#nullable restore
#line 66 "C:\Users\Aykhan\Desktop\CanStellar\YIS\CanStellarBack\CanStellarBack\Views\Home\Index.cshtml"
                                                           Write(item.DurationOfVideo);

#line default
#line hidden
#nullable disable
                WriteLiteral(" s</p></li>\r\n                            </ol>\r\n                        </div>\r\n");
#nullable restore
#line 69 "C:\Users\Aykhan\Desktop\CanStellar\YIS\CanStellarBack\CanStellarBack\Views\Home\Index.cshtml"
                }
                s++;
            

#line default
#line hidden
#nullable disable
                WriteLiteral("\r\n        </div>\r\n    </div>\r\n");
            }
            );
            __Microsoft_AspNetCore_Mvc_Razor_TagHelpers_BodyTagHelper = CreateTagHelper<global::Microsoft.AspNetCore.Mvc.Razor.TagHelpers.BodyTagHelper>();
            __tagHelperExecutionContext.Add(__Microsoft_AspNetCore_Mvc_Razor_TagHelpers_BodyTagHelper);
            await __tagHelperRunner.RunAsync(__tagHelperExecutionContext);
            if (!__tagHelperExecutionContext.Output.IsContentModified)
            {
                await __tagHelperExecutionContext.SetOutputContentAsync();
            }
            Write(__tagHelperExecutionContext.Output);
            __tagHelperExecutionContext = __tagHelperScopeManager.End();
            WriteLiteral("\r\n<script src=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/js/bootstrap.bundle.min.js\" integrity=\"sha384-ENjdO4Dr2bkBIFxQpeoTz1HIcje39Wm4jDKdf19U8gI4ddQ3GYNS7NTKfAdVQSZe\" crossorigin=\"anonymous\"></script>\r\n</html>");
        }
        #pragma warning restore 1998
        [global::Microsoft.AspNetCore.Mvc.Razor.Internal.RazorInjectAttribute]
        public global::Microsoft.AspNetCore.Mvc.ViewFeatures.IModelExpressionProvider ModelExpressionProvider { get; private set; }
        [global::Microsoft.AspNetCore.Mvc.Razor.Internal.RazorInjectAttribute]
        public global::Microsoft.AspNetCore.Mvc.IUrlHelper Url { get; private set; }
        [global::Microsoft.AspNetCore.Mvc.Razor.Internal.RazorInjectAttribute]
        public global::Microsoft.AspNetCore.Mvc.IViewComponentHelper Component { get; private set; }
        [global::Microsoft.AspNetCore.Mvc.Razor.Internal.RazorInjectAttribute]
        public global::Microsoft.AspNetCore.Mvc.Rendering.IJsonHelper Json { get; private set; }
        [global::Microsoft.AspNetCore.Mvc.Razor.Internal.RazorInjectAttribute]
        public global::Microsoft.AspNetCore.Mvc.Rendering.IHtmlHelper<List<Telemetry>> Html { get; private set; }
    }
}
#pragma warning restore 1591
