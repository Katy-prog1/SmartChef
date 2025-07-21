// firestore‑scripts/migrateAmounts.js

const admin = require('firebase-admin');

// Инициализация Admin SDK с вашим ключом
admin.initializeApp({
  credential: admin.credential.cert(
    require('./serviceAccountKey.json')
  )
});

const db = admin.firestore();

async function migrate() {
  const recipesSnapshot = await db.collection('recipes').get();

  for (const doc of recipesSnapshot.docs) {
    const data = doc.data();
    if (!Array.isArray(data.ingredients)) continue;

    // Преобразуем каждый amount из строки в число
    const newIngredients = data.ingredients.map(item => ({
      name: item.name,
      // parseFloat автоматически вернёт NaN → 0
      amount: parseFloat(item.amount) || 0.0
    }));

    // Обновляем документ новыми данными
    await doc.ref.update({ ingredients: newIngredients });
    console.log(`✅ Updated ${doc.id}`);
  }

  console.log('🔄 Миграция завершена');
}

migrate().catch(err => {
  console.error('❌ Ошибка миграции:', err);
  process.exit(1);
});
