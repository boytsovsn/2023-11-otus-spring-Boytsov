// module.exports = {
//     pluginOptions: {
//         proxy: {
//             enabled: true,
//             context: '/api',
//             options: {
//                 target: 'http://localhost:8080',
//                 secure: false,
//                 changeOrigin: true
//             }
//         }
//     }
// }
// module.exports = {
//     // options...
//     devServer: {
//           port: 8080, 
//           proxy: 'http://localhost/api',
//           secure: false,
//           changeOrigin: true
//       }
//   }

// module.exports = {
//     devServer: {
//       proxy: {
//         "/api": {
//           target: "http://localhost:8080",
//           secure: false, 
//           changeOrigin: true
//         }
//       }
//     }
// };