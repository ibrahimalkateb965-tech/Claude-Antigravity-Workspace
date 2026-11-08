---
name: threeui-design-system
description: "Meng To's ThreeUI 3D Web UI Design System — builds immersive 3D web interfaces, interactive canvas heroes, glowing shaders, spatial glassmorphism, and responsive Three.js components with zero memory leaks."
---

# ThreeUI 3D Design System (نظام واجهات الويب ثلاثية الأبعاد)

نظام تصميم وبرمجة واجهات الويب ثلاثية الأبعاد المستوحى من مكتبة ThreeUI مفتوحة المصدر لـ Meng To (@designcodeio/threeui). يركز على الجمع بين جماليات التصميم فائق الحداثة (Dark Neon, Shaders, Glassmorphism, Spatial Depth) والأداء السلس (60 FPS) دون تسريب للذاكرة.

---

## 1. الفلسفة المعمارية للـ Web 3D (Core Architecture)

1. **الدمج الهجين (Hybrid Canvas & DOM):**
   - الـ 3D Canvas يُستخدم كخلفية تفاعلية، أو بطل بصري (Hero Scene)، أو عنصر تفاعلي دقيق (Interactive 3D Card / Badge).
   - النصوص الروابط والأزرار الحيوية تبقى عناصر HTML/DOM حقيقية لدعم سهولة الوصول (Accessibility)، الـ SEO، واختيار النصوص.
2. **التحميل الكسول الإلزامي (Lazy Canvas Mounting):**
   - يُحظر تحميل محرك Three.js في الباندل الأولي بشكل متزامن. يتم استيراده عبر `React.lazy` أو Dynamic Import (`await import('three')`).
3. **التكيف مع العتاد وشاشات الهواتف (Hardware-Adaptive DPR):**
   - ضبط نسبة البكسل لئلا تتجاوز 2: `renderer.setPixelRatio(Math.min(window.devicePixelRatio, 2))`.
   - على الهواتف الضعيفة أو عند انخفاض الـ FPS، يتم إيقاف الجسيمات الكثيفة واستبدالها بتدرج لوني CSS أنيق (Graceful Degradation).

---

## 2. كتالوج المكونات الأساسية (Core Component Patterns)

### 1. بطل الصفحة ثلاثي الأبعاد (3D Hero Canvas)
- **المظهر:** شبكة فراغية ثلاثية الأبعاد عائمة، أو نواة متوهجة (Glowing Core) مع إضاءات PointLight متحركة تتبع مؤشر الفأرة.
- **التفاعل:** استجابة سلسة لزاوية المؤشر (Lerp Mouse Orbit) مع تأثير ارتدادي ناعم عند السكون.

### 2. بطاقات العمق المكاني (Spatial Glassmorphism Cards)
- **المظهر:** طبقات زجاجية نصف شفافة (`backdrop-filter: blur(20px)`)، ذات حدود رفيعة مشعة (`border: 1px solid rgba(255, 255, 255, 0.1)`).
- **التأثير:** زاوية ميلان 3D عند التمرير (Perspective 3D Tilt) مع وميض شعاعي (Specular Highlight) في زاوية الانعكاس.

### 3. خلفيات الجسيمات التفاعلية (Floating Particle Fields)
- **المظهر:** نقاط ضوئية سداسية أو كروية (`THREE.Points`) تسبح ببطء في فضاء الفراغ المظلم.
- **المنطق:** تحديث إحداثيات Y عبر معادلات جيبية (Sine Waves) لإنتاج حركة طبيعية غير متكررة.

---

## 3. المعيار الصارم لمنع تسريب الذاكرة (Memory Cleanup Standard)

> [!CAUTION]
> في بيئات SPA ومشاريع React/Vite، عدم تنظيف كائنات WebGL يؤدي لتراكم سياقات الرسوم وانهيار المتصفح برسالة `WebGL Context Lost`.

### كود دورة الحياة والتنظيف الإلزامي:
```javascript
useEffect(() => {
  let animationFrameId;
  const scene = new THREE.Scene();
  const camera = new THREE.PerspectiveCamera(75, width / height, 0.1, 1000);
  const renderer = new THREE.WebGLRenderer({ 
    canvas: canvasRef.current, 
    alpha: true, 
    antialias: true,
    powerPreference: "high-performance"
  });

  renderer.setSize(width, height);
  renderer.setPixelRatio(Math.min(window.devicePixelRatio, 2));

  // Geometry & Material
  const geometry = new THREE.IcosahedronGeometry(1, 2);
  const material = new THREE.MeshStandardMaterial({ 
    color: 0x6366f1, 
    roughness: 0.2, 
    metalness: 0.8 
  });
  const mesh = new THREE.Mesh(geometry, material);
  scene.add(mesh);

  // Render Loop
  const animate = () => {
    mesh.rotation.x += 0.005;
    mesh.rotation.y += 0.005;
    renderer.render(scene, camera);
    animationFrameId = requestAnimationFrame(animate);
  };
  animate();

  // CLEANUP HOOK - الإلزام الهندسي بالتنظيف
  return () => {
    cancelAnimationFrame(animationFrameId);
    scene.remove(mesh);
    geometry.dispose();
    material.dispose();
    if (material.map) material.map.dispose();
    renderer.dispose();
    renderer.forceContextLoss();
  };
}, []);
```

---

## 4. باليت الألوان والإضاءة المعتمدة (Lighting & Neon Palette)

- **خلفية الفراغ:** `#0a0a14` إلى `#030712`.
- **ألوان الإضاءة النيونية (Neon Accents):**
  - Neon Cyan: `#06b6d4`
  - Cyber Purple: `#a855f7`
  - Radiant Pink: `#ec4899`
  - Emerald Spark: `#10b981`
- **نموذج الإضاءة المثالي:**
  - `AmbientLight`: بكثافة خافتة `0.4` لحماية الظلال.
  - `DirectionalLight`: إضاءة رئيسية بزاوية 45 درجة بلون أبيض نقي.
  - `PointLight` ملون: إضاءة خلفية (Rim Light) بلون نيون لصنع هالة مشعة حول الحواف.

---

## 5. مصفوفة التحقق الذاتي (ThreeUI Self-Check)
- [ ] التأكد من أن المشهد يحقق 60 FPS على الشاشات العادية.
- [ ] فحص كود التنظيف (`dispose`) والتأكد من عدم تراكم الـ Geometries والـ Textures.
- [ ] التأكد من أن النصوص قابلة للاختيار وتدعم الـ RTL في الأزرار والتسميات.
- [ ] وجود بديل CSS خفيف يعمل عند عدم دعم WebGL أو في بيئات البطارية المنخفضة.
