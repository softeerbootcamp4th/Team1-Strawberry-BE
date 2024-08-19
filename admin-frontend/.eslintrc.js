module.exports = {
  parser: '@typescript-eslint/parser', // TypeScript를 사용하는 경우
  extends: [
    'eslint:recommended',
    'plugin:react/recommended',
    'plugin:@typescript-eslint/recommended', // TypeScript 사용 시 추가
    'plugin:prettier/recommended', // Prettier와 통합
  ],
  plugins: ['react', '@typescript-eslint', 'prettier'],
  rules: {
    'prettier/prettier': 'error', // Prettier 규칙을 ESLint 에러로 표시
    // 필요한 다른 규칙 추가
  },
  settings: {
    react: {
      version: 'detect', // React 버전 자동 감지
    },
  },
};
