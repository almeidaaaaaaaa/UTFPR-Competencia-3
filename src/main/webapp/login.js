async function entrar() {
  const email = document.getElementById('email').value.trim();
  const senha = document.getElementById('senha').value;
  const res   = document.getElementById('result');

  if (!email || !senha) {
    res.className = 'result err';
    res.style.display = 'block';
    res.textContent = 'Preencha todos os campos.';
    return;
  }

  res.className = 'result';
  res.style.display = 'block';
  res.textContent = 'Verificando...';

  const params = new URLSearchParams();
  params.append('email', email);
  params.append('senha', senha);

  try {
    const r = await fetch('/api/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      body: params.toString()
    });

    if (r.ok) {
      // O servidor redireciona para a página certa conforme o tipo do usuário
      window.location.href = await r.text();
    } else {
      res.className = 'result err';
      res.textContent = 'E-mail ou senha incorretos.';
    }
  } catch (e) {
    res.className = 'result err';
    res.textContent = 'Falha na conexão: ' + e.message;
  }
}
