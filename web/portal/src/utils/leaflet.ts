export type LatLngTuple = [number, number];

export interface LeafletMap {
  setView(center: LatLngTuple, zoom: number): LeafletMap;
  remove(): void;
  invalidateSize(): void;
  fitBounds(bounds: LatLngTuple[], options?: { padding?: LatLngTuple }): void;
  flyTo(center: LatLngTuple, zoom: number, options?: { duration?: number }): void;
}

export interface LeafletTileLayer {
  addTo(map: LeafletMap): LeafletTileLayer;
  setUrl(url: string): void;
}

export interface LeafletLayerGroup {
  addTo(map: LeafletMap): LeafletLayerGroup;
  clearLayers(): void;
}

export interface LeafletCircleMarker {
  addTo(group: LeafletLayerGroup): LeafletCircleMarker;
  bindPopup(content: string, options?: { maxWidth?: number }): void;
  openPopup(): void;
  on(event: 'click' | 'mouseover' | 'mouseout', handler: () => void): void;
  setStyle(style: { radius?: number; fillColor?: string }): void;
}

export interface LeafletNamespace {
  map(target: string, options?: { preferCanvas?: boolean }): LeafletMap;
  tileLayer(url: string, options?: { subdomains?: string; maxZoom?: number; attribution?: string }): LeafletTileLayer;
  layerGroup(): LeafletLayerGroup;
  circleMarker(center: LatLngTuple, options: {
    radius: number;
    stroke?: boolean;
    fillColor?: string;
    fillOpacity?: number;
    color?: string;
    weight?: number;
  }): LeafletCircleMarker;
}

const leafletScriptUrl = 'https://unpkg.com/leaflet@1.9.4/dist/leaflet.js';
const leafletCssUrl = 'https://unpkg.com/leaflet@1.9.4/dist/leaflet.css';

let leafletPromise: Promise<LeafletNamespace> | null = null;

const appendCss = () => {
  if (document.querySelector(`link[href="${leafletCssUrl}"]`)) return;
  const link = document.createElement('link');
  link.rel = 'stylesheet';
  link.href = leafletCssUrl;
  document.head.appendChild(link);
};

const appendScript = () =>
  new Promise<void>((resolve, reject) => {
    if (document.querySelector(`script[src="${leafletScriptUrl}"]`)) {
      resolve();
      return;
    }
    const script = document.createElement('script');
    script.src = leafletScriptUrl;
    script.async = true;
    script.onload = () => resolve();
    script.onerror = () => reject(new Error('Leaflet 加载失败'));
    document.head.appendChild(script);
  });

export const loadLeaflet = async (): Promise<LeafletNamespace> => {
  if (!leafletPromise) {
    leafletPromise = (async () => {
      appendCss();
      await appendScript();
      const globalLeaflet = (window as Window & { L?: LeafletNamespace }).L;
      if (!globalLeaflet) {
        throw new Error('Leaflet 初始化失败');
      }
      return globalLeaflet;
    })();
  }
  return leafletPromise;
};
