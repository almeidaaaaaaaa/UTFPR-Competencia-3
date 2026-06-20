window.addEventListener('load', () => {
  const nome = sessionStorage.getItem('nomeUsuario') || 'admin';
  document.getElementById('saudacao').textContent = 'Olá, ' + nome;
  carregarUsuarios();
  carregarProjetos();
});

async function carregarUsuarios() {
  const lista = document.getElementById('lista-usuarios');
  try {
    const r = await fetch('/api/usuarios');
    if (!r.ok) { lista.innerHTML = '<p class="vazio">Erro ao carregar usuários.</p>'; return; }
    const usuarios = await r.json();
    if (usuarios.length === 0) { lista.innerHTML = '<p class="vazio">Nenhum usuário cadastrado.</p>'; return; }
    lista.innerHTML = '';
    usuarios.forEach(u => {
      lista.innerHTML += `
        <div class="usuario-item">
          <div>
            <div class="usuario-nome">${u.nome}</div>
            <div class="usuario-email">${u.email}</div>
          </div>
          <div style="display:flex; align-items:center; gap:10px">
            <span class="badge-tipo badge-${u.tipo}">${formatarTipo(u.tipo)}</span>
            <button class="btn-excluir" onclick="excluirUsuario(${u.id}, '${u.nome}')">Excluir</button>
          </div>
        </div>`;
    });
  } catch (e) {
    lista.innerHTML = '<p class="vazio">Falha ao carregar: ' + e.message + '</p>';
  }
}

async function carregarProjetos() {
  const lista = document.getElementById('lista-projetos');
  try {
    const r = await fetch('/api/projetos');
    if (!r.ok) { lista.innerHTML = '<p class="vazio">Erro ao carregar projetos.</p>'; return; }
    const projetos = await r.json();
    if (projetos.length === 0) { lista.innerHTML = '<p class="vazio">Nenhum projeto cadastrado.</p>'; return; }
    lista.innerHTML = '';
    projetos.forEach(p => {
      lista.innerHTML += `
        <div class="projeto-item">
          <div class="projeto-nome">${p.nome}</div>
          <div class="projeto-desc">${p.descricao || 'Sem descrição'}</div>
        </div>`;
    });
  } catch (e) {
    lista.innerHTML = '<p class="vazio">Falha ao carregar: ' + e.message + '</p>';
  }
}

async function cadastrarProjeto() {
  const nome         = document.getElementById('proj-nome').value.trim();
  const descricao    = document.getElementById('proj-descricao').value.trim();
  const coordenador  = document.getElementById('proj-coordenador').value;
  const res          = document.getElementById('result-projeto');

  if (!nome || !coordenador) {
    res.className = 'result err';
    res.style.display = 'block';
    res.textContent = 'Nome e coordenador são obrigatórios.';
    return;
  }

  res.className = 'result';
  res.style.display = 'block';
  res.textContent = 'Salvando...';

  const params = new URLSearchParams();
  params.append('nome', nome);
  params.append('descricao', descricao);
  params.append('idCoordenador', coordenador);

  try {
    const r = await fetch('/api/projetos', {
      method: 'POST',
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      body: params.toString()
    });

    if (r.ok || r.status === 201) {
      res.className = 'result ok';
      res.textContent = 'Projeto cadastrado com sucesso!';
      document.getElementById('proj-nome').value = '';
      document.getElementById('proj-descricao').value = '';
      document.getElementById('proj-coordenador').value = '';
      carregarProjetos();
    } else {
      const text = await r.text();
      res.className = 'result err';
      res.textContent = 'Erro ' + r.status + ': ' + text;
    }
  } catch (e) {
    res.className = 'result err';
    res.textContent = 'Falha na conexão: ' + e.message;
  }
}

async function excluirUsuario(id, nome) {
  if (!confirm('Tem certeza que deseja excluir o usuário "' + nome + '"?')) return;

  try {
    const r = await fetch('/api/usuarios?id=' + id, { method: 'DELETE' });
    if (r.ok) {
      carregarUsuarios();
    } else {
      alert('Erro ao excluir usuário.');
    }
  } catch (e) {
    alert('Falha na conexão: ' + e.message);
  }
}

function formatarTipo(tipo) {
  const map = { COMUM: 'Comunidade', GESTOR: 'Gestor', ADMINISTRADOR: 'Admin' };
  return map[tipo] || tipo;
}

function sair() {
  sessionStorage.clear();
  window.location.href = 'login.html';
}
