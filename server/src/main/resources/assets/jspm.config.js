SystemJS.config({
  paths: {
    "github:": "jspm_packages/github/",
    "npm:": "jspm_packages/npm/",
    "molviewer/": "src/"
  },
  browserConfig: {
    "baseURL": "/"
  },
  devConfig: {
    "map": {
      "plugin-typescript": "github:frankwallis/plugin-typescript@5.2.7",
      "@types/react-dom": "npm:@types/react-dom@0.14.17",
      "@types/mocha": "npm:@types/mocha@2.2.32",
      "mocha": "npm:mocha@3.1.0",
      "@types/chai": "npm:@types/chai@3.4.34",
      "chai": "npm:chai@3.5.0",
      "less": "github:systemjs/plugin-less@0.1.2",
      "css": "github:systemjs/plugin-css@0.1.31",
      "@types/react-bootstrap": "npm:@types/react-bootstrap@0.0.33",
      "@types/jquery": "npm:@types/jquery@2.0.33",
      "@types/redux": "npm:@types/redux@3.6.31",
      "@types/react-redux": "npm:@types/react-redux@4.4.32",
      "@types/redux-saga": "npm:@types/redux-saga@0.9.30",
      "enzyme": "npm:enzyme@2.5.1",
      "@types/enzyme": "npm:@types/enzyme@2.5.37",
      "react-addons-test-utils": "npm:react-addons-test-utils@15.3.2",
      "@types/chai-enzyme": "npm:@types/chai-enzyme@0.5.4",
      "cheerio": "npm:cheerio@0.22.0"
    },
    "packages": {
      "github:frankwallis/plugin-typescript@5.2.7": {
        "map": {
          "typescript": "npm:typescript@2.0.3"
        }
      },
      "npm:@types/react-dom@0.14.17": {
        "map": {
          "@types/react": "npm:@types/react@0.14.44"
        }
      },
      "npm:mocha@3.1.0": {
        "map": {
          "json3": "npm:json3@3.3.2",
          "lodash.create": "npm:lodash.create@3.1.1",
          "debug": "npm:debug@2.2.0",
          "css": "github:systemjs/plugin-css@0.1.31"
        }
      },
      "npm:lodash.create@3.1.1": {
        "map": {
          "lodash._baseassign": "npm:lodash._baseassign@3.2.0",
          "lodash._isiterateecall": "npm:lodash._isiterateecall@3.0.9",
          "lodash._basecreate": "npm:lodash._basecreate@3.0.3"
        }
      },
      "npm:lodash._baseassign@3.2.0": {
        "map": {
          "lodash._basecopy": "npm:lodash._basecopy@3.0.1",
          "lodash.keys": "npm:lodash.keys@3.1.2"
        }
      },
      "npm:debug@2.2.0": {
        "map": {
          "ms": "npm:ms@0.7.1"
        }
      },
      "npm:lodash.keys@3.1.2": {
        "map": {
          "lodash._getnative": "npm:lodash._getnative@3.9.1",
          "lodash.isarray": "npm:lodash.isarray@3.0.4",
          "lodash.isarguments": "npm:lodash.isarguments@3.1.0"
        }
      },
      "npm:chai@3.5.0": {
        "map": {
          "assertion-error": "npm:assertion-error@1.0.2",
          "deep-eql": "npm:deep-eql@0.1.3",
          "type-detect": "npm:type-detect@1.0.0"
        }
      },
      "npm:deep-eql@0.1.3": {
        "map": {
          "type-detect": "npm:type-detect@0.1.1"
        }
      },
      "github:systemjs/plugin-less@0.1.2": {
        "map": {
          "css": "github:systemjs/plugin-css@0.1.30"
        }
      },
      "npm:@types/react-bootstrap@0.0.33": {
        "map": {
          "@types/react": "npm:@types/react@0.14.44"
        }
      },
      "npm:@types/react-redux@4.4.32": {
        "map": {
          "@types/redux": "npm:@types/redux@3.6.31",
          "@types/react": "npm:@types/react@0.14.44"
        }
      },
      "npm:@types/redux-saga@0.9.30": {
        "map": {
          "@types/redux": "npm:@types/redux@3.6.31"
        }
      },
      "npm:enzyme@2.5.1": {
        "map": {
          "object.assign": "npm:object.assign@4.0.4",
          "cheerio": "npm:cheerio@0.22.0",
          "lodash": "npm:lodash@4.16.5",
          "is-subset": "npm:is-subset@0.1.1",
          "object-is": "npm:object-is@1.0.1",
          "object.values": "npm:object.values@1.0.3"
        }
      },
      "npm:cheerio@0.22.0": {
        "map": {
          "dom-serializer": "npm:dom-serializer@0.1.0",
          "css-select": "npm:css-select@1.2.0",
          "entities": "npm:entities@1.1.1",
          "lodash.assignin": "npm:lodash.assignin@4.2.0",
          "lodash.bind": "npm:lodash.bind@4.2.1",
          "lodash.defaults": "npm:lodash.defaults@4.2.0",
          "lodash.flatten": "npm:lodash.flatten@4.4.0",
          "htmlparser2": "npm:htmlparser2@3.9.2",
          "lodash.foreach": "npm:lodash.foreach@4.5.0",
          "lodash.filter": "npm:lodash.filter@4.6.0",
          "lodash.pick": "npm:lodash.pick@4.4.0",
          "lodash.map": "npm:lodash.map@4.6.0",
          "lodash.reduce": "npm:lodash.reduce@4.6.0",
          "lodash.merge": "npm:lodash.merge@4.6.0",
          "lodash.some": "npm:lodash.some@4.6.0",
          "lodash.reject": "npm:lodash.reject@4.6.0"
        }
      },
      "npm:object.assign@4.0.4": {
        "map": {
          "function-bind": "npm:function-bind@1.1.0",
          "define-properties": "npm:define-properties@1.1.2",
          "object-keys": "npm:object-keys@1.0.11"
        }
      },
      "npm:dom-serializer@0.1.0": {
        "map": {
          "entities": "npm:entities@1.1.1",
          "domelementtype": "npm:domelementtype@1.1.3"
        }
      },
      "npm:define-properties@1.1.2": {
        "map": {
          "object-keys": "npm:object-keys@1.0.11",
          "foreach": "npm:foreach@2.0.5"
        }
      },
      "npm:htmlparser2@3.9.2": {
        "map": {
          "domelementtype": "npm:domelementtype@1.3.0",
          "entities": "npm:entities@1.1.1",
          "domutils": "npm:domutils@1.5.1",
          "readable-stream": "npm:readable-stream@2.1.5",
          "inherits": "npm:inherits@2.0.3",
          "domhandler": "npm:domhandler@2.3.0",
          "node-readable-stream": "npm:readable-stream@2.1.5"
        }
      },
      "npm:css-select@1.2.0": {
        "map": {
          "css-what": "npm:css-what@2.1.0",
          "boolbase": "npm:boolbase@1.0.0",
          "nth-check": "npm:nth-check@1.0.1",
          "domutils": "npm:domutils@1.5.1"
        }
      },
      "npm:nth-check@1.0.1": {
        "map": {
          "boolbase": "npm:boolbase@1.0.0"
        }
      },
      "npm:domutils@1.5.1": {
        "map": {
          "dom-serializer": "npm:dom-serializer@0.1.0",
          "domelementtype": "npm:domelementtype@1.3.0"
        }
      },
      "npm:@types/enzyme@2.5.37": {
        "map": {
          "@types/react": "npm:@types/react@0.14.44"
        }
      },
      "npm:domhandler@2.3.0": {
        "map": {
          "domelementtype": "npm:domelementtype@1.3.0"
        }
      },
      "npm:object.values@1.0.3": {
        "map": {
          "define-properties": "npm:define-properties@1.1.2",
          "function-bind": "npm:function-bind@1.1.0",
          "has": "npm:has@1.0.1",
          "es-abstract": "npm:es-abstract@1.6.1"
        }
      },
      "npm:has@1.0.1": {
        "map": {
          "function-bind": "npm:function-bind@1.1.0"
        }
      },
      "npm:es-abstract@1.6.1": {
        "map": {
          "function-bind": "npm:function-bind@1.1.0",
          "is-regex": "npm:is-regex@1.0.3",
          "es-to-primitive": "npm:es-to-primitive@1.1.1",
          "is-callable": "npm:is-callable@1.1.3"
        }
      },
      "npm:es-to-primitive@1.1.1": {
        "map": {
          "is-callable": "npm:is-callable@1.1.3",
          "is-date-object": "npm:is-date-object@1.0.1",
          "is-symbol": "npm:is-symbol@1.0.1"
        }
      },
      "npm:@types/chai-enzyme@0.5.4": {
        "map": {
          "@types/enzyme": "npm:@types/enzyme@2.5.37",
          "@types/react": "npm:@types/react@0.14.44",
          "@types/chai": "npm:@types/chai@3.4.34"
        }
      }
    }
  },
  transpiler: "plugin-typescript",
  packages: {
    "molviewer": {
      "main": "molviewer.tsx",
      "defaultExtension": "tsx",
      "format": "esm",
      "meta": {
        "*.ts": {
          "loader": "plugin-typescript"
        },
        "*.tsx": {
          "loader": "plugin-typescript"
        },
        "*.less": {
          "loader": "less"
        },
        "*.css": {
          "loader": "css"
        }
      }
    }
  },
  separateCSS: true,
  typescriptOptions: {
    "tsconfig": true,
    "typings": {
      "3Dmol": "index.d.ts"
    },
    "typeCheck": "strict"
  }
});

SystemJS.config({
  packageConfigPaths: [
    "github:*/*.json",
    "npm:@*/*.json",
    "npm:*.json"
  ],
  map: {
    "3Dmol": "github:3dmol/3Dmol.js@1.0.6",
    "@types/isomorphic-fetch": "npm:@types/isomorphic-fetch@0.0.31",
    "@types/react": "npm:@types/react@0.14.44",
    "assert": "github:jspm/nodelibs-assert@0.2.0-alpha",
    "bootstrap": "github:twbs/bootstrap@3.3.7",
    "buffer": "github:jspm/nodelibs-buffer@0.2.0-alpha",
    "child_process": "github:jspm/nodelibs-child_process@0.2.0-alpha",
    "constants": "github:jspm/nodelibs-constants@0.2.0-alpha",
    "crypto": "github:jspm/nodelibs-crypto@0.2.0-alpha",
    "domain": "github:jspm/nodelibs-domain@0.2.0-alpha",
    "events": "github:jspm/nodelibs-events@0.2.0-alpha",
    "fs": "github:jspm/nodelibs-fs@0.2.0-alpha",
    "http": "github:jspm/nodelibs-http@0.2.0-alpha",
    "https": "github:jspm/nodelibs-https@0.2.0-alpha",
    "isomorphic-fetch": "npm:isomorphic-fetch@2.2.1",
    "jquery": "npm:jquery@3.1.1",
    "os": "github:jspm/nodelibs-os@0.2.0-alpha",
    "path": "github:jspm/nodelibs-path@0.2.0-alpha",
    "process": "github:jspm/nodelibs-process@0.2.0-alpha",
    "react": "npm:react@15.3.2",
    "react-bootstrap": "npm:react-bootstrap@0.30.5",
    "react-dom": "npm:react-dom@15.3.2",
    "react-redux": "npm:react-redux@4.4.5",
    "redux": "npm:redux@3.6.0",
    "redux-saga": "npm:redux-saga@0.12.0",
    "stream": "github:jspm/nodelibs-stream@0.2.0-alpha",
    "string_decoder": "github:jspm/nodelibs-string_decoder@0.2.0-alpha",
    "url": "github:jspm/nodelibs-url@0.2.0-alpha",
    "util": "github:jspm/nodelibs-util@0.2.0-alpha",
    "vm": "github:jspm/nodelibs-vm@0.2.0-alpha",
    "zlib": "github:jspm/nodelibs-zlib@0.2.0-alpha"
  },
  packages: {
    "github:jspm/nodelibs-os@0.2.0-alpha": {
      "map": {
        "os-browserify": "npm:os-browserify@0.2.1"
      }
    },
    "github:jspm/nodelibs-crypto@0.2.0-alpha": {
      "map": {
        "crypto-browserify": "npm:crypto-browserify@3.11.0"
      }
    },
    "npm:crypto-browserify@3.11.0": {
      "map": {
        "browserify-cipher": "npm:browserify-cipher@1.0.0",
        "create-ecdh": "npm:create-ecdh@4.0.0",
        "browserify-sign": "npm:browserify-sign@4.0.0",
        "diffie-hellman": "npm:diffie-hellman@5.0.2",
        "randombytes": "npm:randombytes@2.0.3",
        "inherits": "npm:inherits@2.0.3",
        "public-encrypt": "npm:public-encrypt@4.0.0",
        "create-hmac": "npm:create-hmac@1.1.4",
        "create-hash": "npm:create-hash@1.1.2",
        "pbkdf2": "npm:pbkdf2@3.0.9"
      }
    },
    "npm:browserify-sign@4.0.0": {
      "map": {
        "create-hmac": "npm:create-hmac@1.1.4",
        "create-hash": "npm:create-hash@1.1.2",
        "inherits": "npm:inherits@2.0.3",
        "parse-asn1": "npm:parse-asn1@5.0.0",
        "browserify-rsa": "npm:browserify-rsa@4.0.1",
        "elliptic": "npm:elliptic@6.3.2",
        "bn.js": "npm:bn.js@4.11.6"
      }
    },
    "npm:diffie-hellman@5.0.2": {
      "map": {
        "randombytes": "npm:randombytes@2.0.3",
        "miller-rabin": "npm:miller-rabin@4.0.0",
        "bn.js": "npm:bn.js@4.11.6"
      }
    },
    "npm:public-encrypt@4.0.0": {
      "map": {
        "create-hash": "npm:create-hash@1.1.2",
        "randombytes": "npm:randombytes@2.0.3",
        "parse-asn1": "npm:parse-asn1@5.0.0",
        "browserify-rsa": "npm:browserify-rsa@4.0.1",
        "bn.js": "npm:bn.js@4.11.6"
      }
    },
    "npm:browserify-cipher@1.0.0": {
      "map": {
        "browserify-des": "npm:browserify-des@1.0.0",
        "evp_bytestokey": "npm:evp_bytestokey@1.0.0",
        "browserify-aes": "npm:browserify-aes@1.0.6"
      }
    },
    "npm:create-ecdh@4.0.0": {
      "map": {
        "elliptic": "npm:elliptic@6.3.2",
        "bn.js": "npm:bn.js@4.11.6"
      }
    },
    "npm:create-hash@1.1.2": {
      "map": {
        "inherits": "npm:inherits@2.0.3",
        "sha.js": "npm:sha.js@2.4.5",
        "ripemd160": "npm:ripemd160@1.0.1",
        "cipher-base": "npm:cipher-base@1.0.3"
      }
    },
    "npm:pbkdf2@3.0.9": {
      "map": {
        "create-hmac": "npm:create-hmac@1.1.4"
      }
    },
    "npm:create-hmac@1.1.4": {
      "map": {
        "create-hash": "npm:create-hash@1.1.2",
        "inherits": "npm:inherits@2.0.3"
      }
    },
    "npm:browserify-des@1.0.0": {
      "map": {
        "cipher-base": "npm:cipher-base@1.0.3",
        "inherits": "npm:inherits@2.0.3",
        "des.js": "npm:des.js@1.0.0"
      }
    },
    "npm:browserify-aes@1.0.6": {
      "map": {
        "cipher-base": "npm:cipher-base@1.0.3",
        "create-hash": "npm:create-hash@1.1.2",
        "evp_bytestokey": "npm:evp_bytestokey@1.0.0",
        "inherits": "npm:inherits@2.0.3",
        "buffer-xor": "npm:buffer-xor@1.0.3"
      }
    },
    "npm:browserify-rsa@4.0.1": {
      "map": {
        "randombytes": "npm:randombytes@2.0.3",
        "bn.js": "npm:bn.js@4.11.6"
      }
    },
    "npm:evp_bytestokey@1.0.0": {
      "map": {
        "create-hash": "npm:create-hash@1.1.2"
      }
    },
    "npm:miller-rabin@4.0.0": {
      "map": {
        "bn.js": "npm:bn.js@4.11.6",
        "brorand": "npm:brorand@1.0.6"
      }
    },
    "npm:parse-asn1@5.0.0": {
      "map": {
        "create-hash": "npm:create-hash@1.1.2",
        "browserify-aes": "npm:browserify-aes@1.0.6",
        "evp_bytestokey": "npm:evp_bytestokey@1.0.0",
        "pbkdf2": "npm:pbkdf2@3.0.9",
        "asn1.js": "npm:asn1.js@4.8.1"
      }
    },
    "npm:elliptic@6.3.2": {
      "map": {
        "inherits": "npm:inherits@2.0.3",
        "bn.js": "npm:bn.js@4.11.6",
        "brorand": "npm:brorand@1.0.6",
        "hash.js": "npm:hash.js@1.0.3"
      }
    },
    "npm:sha.js@2.4.5": {
      "map": {
        "inherits": "npm:inherits@2.0.3"
      }
    },
    "npm:cipher-base@1.0.3": {
      "map": {
        "inherits": "npm:inherits@2.0.3"
      }
    },
    "npm:hash.js@1.0.3": {
      "map": {
        "inherits": "npm:inherits@2.0.3"
      }
    },
    "npm:des.js@1.0.0": {
      "map": {
        "inherits": "npm:inherits@2.0.3",
        "minimalistic-assert": "npm:minimalistic-assert@1.0.0"
      }
    },
    "npm:asn1.js@4.8.1": {
      "map": {
        "bn.js": "npm:bn.js@4.11.6",
        "inherits": "npm:inherits@2.0.3",
        "minimalistic-assert": "npm:minimalistic-assert@1.0.0"
      }
    },
    "github:jspm/nodelibs-buffer@0.2.0-alpha": {
      "map": {
        "buffer-browserify": "npm:buffer@4.9.1"
      }
    },
    "github:jspm/nodelibs-stream@0.2.0-alpha": {
      "map": {
        "stream-browserify": "npm:stream-browserify@2.0.1"
      }
    },
    "npm:stream-browserify@2.0.1": {
      "map": {
        "inherits": "npm:inherits@2.0.3",
        "readable-stream": "npm:readable-stream@2.1.5"
      }
    },
    "npm:buffer@4.9.1": {
      "map": {
        "base64-js": "npm:base64-js@1.2.0",
        "isarray": "npm:isarray@1.0.0",
        "ieee754": "npm:ieee754@1.1.8"
      }
    },
    "github:jspm/nodelibs-string_decoder@0.2.0-alpha": {
      "map": {
        "string_decoder-browserify": "npm:string_decoder@0.10.31"
      }
    },
    "npm:readable-stream@2.1.5": {
      "map": {
        "inherits": "npm:inherits@2.0.3",
        "string_decoder": "npm:string_decoder@0.10.31",
        "isarray": "npm:isarray@1.0.0",
        "process-nextick-args": "npm:process-nextick-args@1.0.7",
        "util-deprecate": "npm:util-deprecate@1.0.2",
        "core-util-is": "npm:core-util-is@1.0.2",
        "buffer-shims": "npm:buffer-shims@1.0.0"
      }
    },
    "npm:react@15.3.2": {
      "map": {
        "loose-envify": "npm:loose-envify@1.3.0",
        "object-assign": "npm:object-assign@4.1.0",
        "fbjs": "npm:fbjs@0.8.5"
      }
    },
    "npm:fbjs@0.8.5": {
      "map": {
        "loose-envify": "npm:loose-envify@1.3.0",
        "object-assign": "npm:object-assign@4.1.0",
        "promise": "npm:promise@7.1.1",
        "isomorphic-fetch": "npm:isomorphic-fetch@2.2.1",
        "ua-parser-js": "npm:ua-parser-js@0.7.10",
        "core-js": "npm:core-js@1.2.7",
        "immutable": "npm:immutable@3.8.1"
      }
    },
    "npm:loose-envify@1.2.0": {
      "map": {
        "js-tokens": "npm:js-tokens@1.0.3"
      }
    },
    "npm:promise@7.1.1": {
      "map": {
        "asap": "npm:asap@2.0.5"
      }
    },
    "npm:isomorphic-fetch@2.2.1": {
      "map": {
        "node-fetch": "npm:node-fetch@1.6.3",
        "whatwg-fetch": "npm:whatwg-fetch@1.0.0"
      }
    },
    "npm:node-fetch@1.6.3": {
      "map": {
        "is-stream": "npm:is-stream@1.1.0",
        "encoding": "npm:encoding@0.1.12"
      }
    },
    "github:twbs/bootstrap@3.3.7": {
      "map": {
        "jquery": "npm:jquery@3.1.1"
      }
    },
    "npm:encoding@0.1.12": {
      "map": {
        "iconv-lite": "npm:iconv-lite@0.4.13"
      }
    },
    "github:jspm/nodelibs-domain@0.2.0-alpha": {
      "map": {
        "domain-browserify": "npm:domain-browser@1.1.7"
      }
    },
    "github:jspm/nodelibs-zlib@0.2.0-alpha": {
      "map": {
        "zlib-browserify": "npm:browserify-zlib@0.1.4"
      }
    },
    "github:jspm/nodelibs-http@0.2.0-alpha": {
      "map": {
        "http-browserify": "npm:stream-http@2.4.1"
      }
    },
    "github:jspm/nodelibs-url@0.2.0-alpha": {
      "map": {
        "url-browserify": "npm:url@0.11.0"
      }
    },
    "npm:browserify-zlib@0.1.4": {
      "map": {
        "readable-stream": "npm:readable-stream@2.1.5",
        "pako": "npm:pako@0.2.9"
      }
    },
    "npm:url@0.11.0": {
      "map": {
        "querystring": "npm:querystring@0.2.0",
        "punycode": "npm:punycode@1.3.2"
      }
    },
    "npm:react-bootstrap@0.30.5": {
      "map": {
        "babel-runtime": "npm:babel-runtime@6.11.6",
        "dom-helpers": "npm:dom-helpers@2.4.0",
        "classnames": "npm:classnames@2.2.5",
        "keycode": "npm:keycode@2.1.7",
        "react-prop-types": "npm:react-prop-types@0.4.0",
        "invariant": "npm:invariant@2.2.1",
        "react-overlays": "npm:react-overlays@0.6.10",
        "uncontrollable": "npm:uncontrollable@4.0.3",
        "warning": "npm:warning@3.0.0"
      }
    },
    "npm:react-prop-types@0.4.0": {
      "map": {
        "warning": "npm:warning@3.0.0"
      }
    },
    "npm:react-overlays@0.6.10": {
      "map": {
        "classnames": "npm:classnames@2.2.5",
        "dom-helpers": "npm:dom-helpers@2.4.0",
        "react-prop-types": "npm:react-prop-types@0.4.0",
        "warning": "npm:warning@3.0.0"
      }
    },
    "npm:uncontrollable@4.0.3": {
      "map": {
        "invariant": "npm:invariant@2.2.1"
      }
    },
    "npm:babel-runtime@6.11.6": {
      "map": {
        "core-js": "npm:core-js@2.4.1",
        "regenerator-runtime": "npm:regenerator-runtime@0.9.5"
      }
    },
    "npm:invariant@2.2.1": {
      "map": {
        "loose-envify": "npm:loose-envify@1.2.0"
      }
    },
    "npm:warning@3.0.0": {
      "map": {
        "loose-envify": "npm:loose-envify@1.2.0"
      }
    },
    "npm:react-redux@4.4.5": {
      "map": {
        "lodash": "npm:lodash@4.16.4",
        "hoist-non-react-statics": "npm:hoist-non-react-statics@1.2.0",
        "loose-envify": "npm:loose-envify@1.2.0",
        "invariant": "npm:invariant@2.2.1"
      }
    },
    "npm:redux@3.6.0": {
      "map": {
        "lodash": "npm:lodash@4.16.4",
        "loose-envify": "npm:loose-envify@1.2.0",
        "symbol-observable": "npm:symbol-observable@1.0.4",
        "lodash-es": "npm:lodash-es@4.16.4"
      }
    },
    "npm:loose-envify@1.3.0": {
      "map": {
        "js-tokens": "npm:js-tokens@2.0.0"
      }
    },
    "npm:stream-http@2.4.1": {
      "map": {
        "builtin-status-codes": "npm:builtin-status-codes@2.0.0",
        "readable-stream": "npm:readable-stream@2.1.5",
        "inherits": "npm:inherits@2.0.3",
        "xtend": "npm:xtend@4.0.1",
        "to-arraybuffer": "npm:to-arraybuffer@1.0.1"
      }
    }
  }
});
