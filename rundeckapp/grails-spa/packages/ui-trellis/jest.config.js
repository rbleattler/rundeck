const esModules = [
  "vue-virtual-scroller",
  "uuid",
    "primevue/.*",
    "@primevue/.*",
    "@primeuix/.*",
].join("|");

module.exports = {
  moduleFileExtensions: ["js", "ts", "vue", "mjs"],
  transform: {
    "^.+\\.tsx?$": ["ts-jest", { tsconfig: "tsconfig.app.json" }],
    "^.+\\.vue$": "@vue/vue3-jest",
    "^.+\\.(mj|j|t)s$": "babel-jest",
  },
  setupFiles: ["<rootDir>/setupTests.js"],
  setupFilesAfterEnv: ["<rootDir>/jest.global-mocks.js"],
  roots: ["<rootDir>/src/app", "<rootDir>/src/library", "<rootDir>/tests"],
  moduleNameMapper: {
    "\\.(css|less|sass|scss)$": "identity-obj-proxy",
    "^@/(.*)$": "<rootDir>/src/$1",
    "^primevue/(.*)": "<rootDir>/node_modules/primevue/$1",
    "^@primevue/(.*)": "<rootDir>/node_modules/@primevue/$1",
    "^@primeuix/(.*)": "<rootDir>/node_modules/@primeuix/$1",
  },
  modulePathIgnorePatterns: ["<rootDir>/public"],
  testMatch: ["**/*.test.ts", "**/*.spec.ts"],
  verbose: true,
  testEnvironment: "@happy-dom/jest-environment",
  transformIgnorePatterns: [`/node_modules/(?!${esModules})`],
  clearMocks: true,
  reporters: ["default", "jest-junit"],
};
