import { ViewConfig } from '@vaadin/hilla-file-router/types.js';
import { Button, ComboBox, DatePicker, Dialog, Grid, GridColumn, GridItemModel, GridSortColumn, HorizontalLayout, Icon, NumberField, Select, TextField, VerticalLayout } from '@vaadin/react-components';
import { Notification } from '@vaadin/react-components/Notification';
import { CancionServices, TaskService } from 'Frontend/generated/endpoints';
import { useSignal } from '@vaadin/hilla-react-signals';
import handleError from 'Frontend/views/_ErrorHandler';
import { Group, ViewToolbar } from 'Frontend/components/ViewToolbar';

import { useDataProvider } from '@vaadin/hilla-react-crud';

import { useCallback, useEffect, useState } from 'react';

export const config: ViewConfig = {
  title: 'Cancion',
  menu: {
    icon: 'vaadin:clipboard-check',
    order: 1,
    title: 'Cancion',
  },
};

type Cancion = {
  id: number;
  nombre: string;
  genero: string;
  album: string;
  duracion: number;
  url: string;
  tipo: string;
};

type CancionEntryFormProps = {
  onCancionCreated?: () => void;
};

type CancionEntryFormPropsUpdate = {
  cancion: Cancion;
  onCancionUpdated?: () => void;
};

//GUARDAR CANCION
function CancionEntryForm(props: CancionEntryFormProps) {
  const nombre = useSignal('');
  const genero = useSignal('');
  const album = useSignal('');
  const duracion = useSignal('');
  const url = useSignal('');
  const tipo = useSignal('');
  const createCancion = async () => {
    try {
      if (nombre.value.trim().length > 0 && genero.value.trim().length > 0) {
        const id_genero = parseInt(genero.value);
        const id_album = parseInt(album.value);
        await CancionServices.create(nombre.value, id_genero, parseInt(duracion.value), url.value, tipo.value, id_album);
        if (props.onCancionCreated) {
          props.onCancionCreated();
        }
        nombre.value = '';
        genero.value = '';
        album.value = '';
        duracion.value = '';
        url.value = '';
        tipo.value = '';

        dialogOpened.value = false;
        Notification.show('Cancion creada', { duration: 5000, position: 'bottom-end', theme: 'success' });
      } else {
        Notification.show('No se pudo crear, faltan datos', { duration: 5000, position: 'top-center', theme: 'error' });
      }

    } catch (error) {
      console.log(error);
      handleError(error);
    }
  };

  let listaGenero = useSignal<String[]>([]);
  useEffect(() => {
    CancionServices.listaGeneroCombo().then(data =>
      //console.log(data)
      listaGenero.value = data
    );
  }, []);
  let listaAlbum = useSignal<String[]>([]);
  useEffect(() => {
    CancionServices.listaAlbumCombo().then(data =>
      //console.log(data)
      listaAlbum.value = data
    );
  }, []);

  let listaTipo = useSignal<String[]>([]);
  useEffect(() => {
    CancionServices.listTipo().then(data =>
      //console.log(data)
      listaTipo.value = data
    );
  }, []);
  const dialogOpened = useSignal(false);
  return (
    <>
      <Dialog
        modeless
        headerTitle="Nueva Canción"
        opened={dialogOpened.value}
        onOpenedChanged={({ detail }) => {
          dialogOpened.value = detail.value;
        }}
        footer={
          <>
            <Button
              onClick={() => {
                dialogOpened.value = false;
              }}
            >
              Cancelar
            </Button>
            <Button onClick={createCancion} theme="primary">
              Registrar
            </Button>

          </>
        }
      >
        <VerticalLayout style={{ alignItems: 'stretch', width: '18rem', maxWidth: '100%' }}>
          <TextField label="Nombre de la canción"
            placeholder="Ingrese el nombre de la canción"
            aria-label="Nombre de la canción"
            value={nombre.value}
            onValueChanged={(evt) => (nombre.value = evt.detail.value)}
          />
          <ComboBox label="Genero"
            items={listaGenero.value}
            placeholder='Seleccione un genero'
            aria-label='Seleccione un genero de la lista'
            value={genero.value}
            onValueChanged={(evt) => (genero.value = evt.detail.value)}
          />
          <ComboBox label="Album"
            items={listaAlbum.value}
            placeholder='Seleccione un album'
            aria-label='Seleccione un album de la lista'
            value={album.value}
            onValueChanged={(evt) => (album.value = evt.detail.value)}
          />
          <ComboBox label="Tipo"
            items={listaTipo.value}
            placeholder='Seleccione un tipo de archivo'
            aria-label='Seleccione un tipo de archivo de la lista'
            value={tipo.value}
            onValueChanged={(evt) => (tipo.value = evt.detail.value)}
          />
          <NumberField label="Duracion de la canción"
            placeholder="Ingrese la duración de la canción"
            aria-label="Duracion de la canción"
            value={duracion.value}
            onValueChanged={(evt) => (duracion.value = evt.detail.value)}
          />
          <TextField label="Url de la canción"
            placeholder="Ingrese la Url de la canción"
            aria-label="Url de la canción"
            value={url.value}
            onValueChanged={(evt) => (url.value = evt.detail.value)}
          />
        </VerticalLayout>
      </Dialog>
      <Button
        onClick={() => {
          dialogOpened.value = true;
        }}
      >
        Agregar
      </Button>
    </>
  );
}

// ACTUALIZAR CANCION
function CancionEntryFormUpdate(props: CancionEntryFormPropsUpdate) {
  const dialogOpened = useSignal(false);

  // Inicializa los campos con los datos de la canción
  const id = useSignal(props.cancion?.id?.toString() || '');
  const nombre = useSignal(props.cancion?.nombre || '');
  const genero = useSignal(props.cancion?.genero || '');
  const album = useSignal(props.cancion?.album || '');
  const duracion = useSignal(props.cancion?.duracion?.toString() || '');
  const url = useSignal(props.cancion?.url || '');
  const tipo = useSignal(props.cancion?.tipo || '');

  // Cuando se abre el diálogo, actualiza los valores
  const openDialog = () => {
    id.value = props.cancion?.id?.toString() || '';
    nombre.value = props.cancion?.nombre || '';
    genero.value = props.cancion?.genero || '';
    album.value = props.cancion?.album || '';
    duracion.value = props.cancion?.duracion?.toString() || '';
    url.value = props.cancion?.url || '';
    tipo.value = props.cancion?.tipo || '';
    dialogOpened.value = true;
  };

  const updateCancion = async () => {
    try {
      if (nombre.value.trim().length > 0 && genero.value.trim().length > 0) {
        const id_genero = parseInt(genero.value);
        const id_album = parseInt(album.value);
        await CancionServices.update(
          parseInt(id.value),
          nombre.value,
          id_genero,
          parseInt(duracion.value),
          url.value,
          tipo.value,
          id_album
        );
        if (props.onCancionUpdated) {
          props.onCancionUpdated();
        }
        dialogOpened.value = false;
        Notification.show('Cancion editada', { duration: 5000, position: 'bottom-end', theme: 'success' });
      } else {
        Notification.show('No se pudo editar, faltan datos', { duration: 5000, position: 'top-center', theme: 'error' });
      }
    } catch (error) {
      console.log(error);
      handleError(error);
    }
  };

  let listaGenero = useSignal<String[]>([]);
  useEffect(() => {
    CancionServices.listaGeneroCombo().then(data =>
      //console.log(data)
      listaGenero.value = data
    );
  }, []);
  let listaAlbum = useSignal<String[]>([]);
  useEffect(() => {
    CancionServices.listaAlbumCombo().then(data =>
      //console.log(data)
      listaAlbum.value = data
    );
  }, []);

  let listaTipo = useSignal<String[]>([]);
  useEffect(() => {
    CancionServices.listTipo().then(data =>
      //console.log(data)
      listaTipo.value = data
    );
  }, []);

  return (
    <>
      <Dialog
        modeless
        headerTitle="Editar Canción"
        opened={dialogOpened.value}
        onOpenedChanged={({ detail }) => {
          dialogOpened.value = detail.value;
        }}
        footer={
          <>
            <Button onClick={() => { dialogOpened.value = false; }}>Cancelar</Button>
            <Button onClick={updateCancion} theme="primary">Registrar</Button>
          </>
        }
      >
        <VerticalLayout style={{ alignItems: 'stretch', width: '18rem', maxWidth: '100%' }}>
          <TextField label="Nombre de la canción"
            placeholder="Ingrese el nombre de la canción"
            aria-label="Nombre de la canción"
            value={nombre.value}
            defaultValue={nombre.value}
            onValueChanged={(evt) => (nombre.value = evt.detail.value)}
          />
          <ComboBox label="Genero"
            items={listaGenero.value}
            placeholder='Seleccione un genero'
            aria-label='Seleccione un genero de la lista'
            value={genero.value}
            defaultValue={genero.value}
            onValueChanged={(evt) => (genero.value = evt.detail.value)}
          />
          <ComboBox label="Album"
            items={listaAlbum.value}
            placeholder='Seleccione un album'
            aria-label='Seleccione un album de la lista'
            value={album.value}
            defaultValue={album.value}
            onValueChanged={(evt) => (album.value = evt.detail.value)}
          />
          <ComboBox label="Tipo"
            items={listaTipo.value}
            placeholder='Seleccione un tipo de archivo'
            aria-label='Seleccione un tipo de archivo de la lista'
            value={tipo.value}
            defaultValue={tipo.value}
            onValueChanged={(evt) => (tipo.value = evt.detail.value)}
          />
          <NumberField label="Duracion de la canción"
            placeholder="Ingrese la duración de la canción"
            aria-label="Duracion de la canción"
            value={duracion.value}
            defaultValue={duracion.value}
            onValueChanged={(evt) => (duracion.value = evt.detail.value)}
          />
          <TextField label="Url de la canción"
            placeholder="Ingrese la Url de la canción"
            aria-label="Url de la canción"
            value={url.value}
            defaultValue={url.value}
            onValueChanged={(evt) => (url.value = evt.detail.value)}
          />
        </VerticalLayout>
      </Dialog>
      <Button onClick={openDialog}>Editar</Button>
    </>
  );
}


//LISTA DE CANCIONES
export default function CancionView() {
  const [items, setItems] = useState([]);
  useEffect(() => {
    CancionServices.listAll().then(function (data) {
      //console.log(data);
      setItems(data);
    });
  }, []);

  const order = (event, columnId) => {
    const direction = event.detail.value;
    if (!direction) {
      // Sin orden, mostrar lista original
      CancionServices.listAll().then(setItems);
    } else {
      var dir = (direction == 'asc') ? 1 : 2;
      CancionServices.order(columnId, dir).then(setItems);
    }
  }

  const callData = () => {
    CancionServices.listAll().then(function (data) {
      //console.log(data);
      setItems(data);
    });
  }

  function indexLink({ model }: { model: GridItemModel<Cancion> }) {
    return (
      <span>
        <CancionEntryFormUpdate cancion={model.item} onCancionUpdated={callData} />
      </span>
    );
  }


  function indexIndex({ model }: { model: GridItemModel<Cancion> }) {
    return (
      <span>
        {model.index + 1}
      </span>
    );
  }

  //Busqueda
  const criterio = useSignal('');
  const texto = useSignal('');

  const itemSelect = [
    {
      label: 'Cancion',
      value: 'nombre',
    },
    {
      label: 'Genero',
      value: 'genero',
    },
    {
      label: 'Album',
      value: 'album',
    },
    {
      label: 'Duracion',
      value: 'duracion',
    },
    {
      label: 'Tipo',
      value: 'tipo',
    }
  ];

  const search = async () => {
    try {
      CancionServices.search(criterio.value, texto.value, 0).then(function (data) {
        setItems(data);
      });

      criterio.value = '';
      texto.value = '';

      Notification.show('Busqueda realizada', { duration: 5000, position: 'bottom-end', theme: 'success' });


    } catch (error) {
      console.log(error);
      handleError(error);
    }
  };

  return (

    <main className="w-full h-full flex flex-col box-border gap-s p-m">

      <ViewToolbar title="Lista de Canciones">
        <Group>
          <CancionEntryForm onCancionCreated={callData} />
        </Group>
      </ViewToolbar>
      <HorizontalLayout theme="spacing">
        <Select items={itemSelect}
          value={criterio.value}
          onValueChanged={(evt) => (criterio.value = evt.detail.value)}
          placeholder={'Seleccione un criterio'}>

        </Select>

        <TextField
          placeholder="Search"
          style={{ width: '50%' }}
          value={texto.value}
          onValueChanged={(evt) => (texto.value = evt.detail.value)}

        >
          <Icon slot="prefix" icon="vaadin:search" />
        </TextField>
        <Button onClick={search} theme="primary">
          BUSCAR
        </Button>


      </HorizontalLayout>
      <Grid items={items}>
        <GridColumn renderer={indexIndex} header="Nro" />
        <GridSortColumn onDirectionChanged={(e) => order(e, "nombre")} path="nombre" header="Cancion" />
        <GridSortColumn onDirectionChanged={(e) => order(e, "genero")} path="genero" header="Genero" />
        <GridSortColumn onDirectionChanged={(e) => order(e, "album")} path="album" header="Album" />
        <GridSortColumn onDirectionChanged={(e) => order(e, "duracion")} path="duracion" header="Duracion" />
        <GridColumn path="url" header="Url" />
        <GridSortColumn onDirectionChanged={(e) => order(e, "tipo")} path="tipo" header="Tipo" />
        <GridColumn header="Acciones" renderer={indexLink} />

      </Grid>
    </main>
  );
}
