const MiniCssExtractPlugin = require("mini-css-extract-plugin");
const path = require("path");
const VueLoaderPlugin = require("vue-loader/lib/plugin");

const jsSrcDir = "./src/main/resources/js/";

module.exports = (env, args) => {
    const devmode = args.mode === "development";
    return {
        devtool: devmode ? "source-map" : "none",
        entry: {
            app: jsSrcDir + "app.js",
            "channels/home": jsSrcDir + "channels.home.js",
            "channels/videos": jsSrcDir + "channels.videos.js",
            "channels/videos/upload": jsSrcDir + "channels.videos.upload.js",
            index: jsSrcDir + "index.js",
            login: jsSrcDir + "login.js",
            register: jsSrcDir + "register.js",
            watch: jsSrcDir + "watch.js"
        },
        module: {
            rules: [{
                    test: /\.vue$/,
                    loader: "vue-loader"
                },
                {
                    test: /\.js$/,
                    exclude: /(node_modules|bower_components)/,
                    use: {
                        loader: "babel-loader",
                        options: {
                            presets: ["@babel/preset-env"]
                        }
                    }
                },
                {
                    test: /\.css$/,
                    use: ["style-loader", "vue-style-loader", "css-loader"]
                },
                {
                    test: /\.scss/,
                    use: ["vue-style-loader", "css-loader", "sass-loader", {
                        loader: "sass-resources-loader",
                        options: {
                            resources: [path.resolve(__dirname, "./src/main/resources/sass/_variables.scss")]
                        }
                    }]
                }
            ]
        },
        output: {
            filename: "[name].js",
            path: __dirname + "/src/main/resources/static/js"
        },
        plugins: [
            new MiniCssExtractPlugin({
                filename: "../css/[name].css"
            }),
            new VueLoaderPlugin()
        ],
        resolve: {
            extensions: [".js", ".vue"],
            alias: {
                vue$: "vue/dist/vue.esm.js"
            }
        }
    };
};