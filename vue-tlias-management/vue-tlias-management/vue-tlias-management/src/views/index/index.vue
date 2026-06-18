<script setup>
import homeImage from '@/assets/background.png'
</script>

<template>
  <div class="home-page">
    <div class="bg-layer bg-layer-one"></div>
    <div class="bg-layer bg-layer-two"></div>
    <div class="home-stage">
      <div class="home-frame">
        <img class="home-image" :src="homeImage" alt="首页展示图" />
      </div>
    </div>
  </div>
</template>

<style scoped>
.home-page {
  position: relative;
  width: 100%;
  height: 100%;
  min-height: calc(100vh - 60px);
  overflow: hidden;
  background:
    radial-gradient(circle at 18% 20%, rgba(0, 170, 160, 0.14), transparent 30%),
    radial-gradient(circle at 82% 24%, rgba(0, 127, 164, 0.14), transparent 32%),
    linear-gradient(180deg, #eef6fb 0%, #f7fbff 100%);
}

.bg-layer {
  position: absolute;
  border-radius: 999px;
  filter: blur(2px);
  pointer-events: none;
}

.bg-layer-one {
  width: 360px;
  height: 360px;
  top: -90px;
  left: -100px;
  background: radial-gradient(circle, rgba(0, 170, 160, 0.18) 0%, rgba(0, 170, 160, 0.02) 70%, transparent 100%);
}

.bg-layer-two {
  width: 420px;
  height: 420px;
  right: -120px;
  bottom: -140px;
  background: radial-gradient(circle, rgba(0, 84, 125, 0.16) 0%, rgba(0, 84, 125, 0.03) 70%, transparent 100%);
}

.home-stage {
  position: relative;
  z-index: 1;
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 28px;
  box-sizing: border-box;
}

.home-frame {
  width: min(100%, 1200px);
  height: min(100%, 760px);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 18px;
  border-radius: 28px;
  background: rgba(255, 255, 255, 0.52);
  border: 1px solid rgba(255, 255, 255, 0.7);
  box-shadow: 0 24px 70px rgba(15, 23, 42, 0.12);
  backdrop-filter: blur(8px);
  box-sizing: border-box;
}

.home-image {
  display: block;
  max-width: 100%;
  max-height: 100%;
  width: auto;
  height: auto;
  object-fit: contain;
  object-position: center;
}

@media (max-width: 900px) {
  .home-stage {
    padding: 16px;
  }

  .home-frame {
    padding: 12px;
    border-radius: 22px;
  }
}

@media (max-width: 640px) {
  .home-frame {
    width: 100%;
    height: auto;
    min-height: 420px;
  }
}

</style>