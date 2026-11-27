/* TechStore - interacción mínima */
(function () {
  // Selector de cantidad en el detalle del producto
  const cant = document.getElementById('cantidad');
  if (cant) {
    const max = parseInt(cant.max) || 99;
    document.getElementById('menos').addEventListener('click', () => cant.value = Math.max(1, cant.value - 1));
    document.getElementById('mas').addEventListener('click', () => cant.value = Math.min(max, +cant.value + 1));
  }
  // Confirmación antes de eliminar (formularios con data-confirm)
  document.querySelectorAll('form[data-confirm]').forEach(f =>
    f.addEventListener('submit', e => { if (!confirm(f.dataset.confirm)) e.preventDefault(); }));
})();
