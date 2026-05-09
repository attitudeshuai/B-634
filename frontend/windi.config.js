import { defineConfig } from 'windicss/helpers'

export default defineConfig({
  theme: {
    extend: {
      colors: {
        primary: {
          DEFAULT: '#961E19',  // 深红色 RGB(150, 30, 25)
          dark: '#7A1815',
          light: '#B8332D'
        }
      },
      fontFamily: {
        sans: ['Microsoft YaHei', 'PingFang SC', 'Helvetica Neue', 'Arial', 'sans-serif']
      }
    }
  }
})
