import { VisionCameraProxy, Frame } from 'react-native-vision-camera';

const plugin = VisionCameraProxy.getFrameProcessorPlugin('qr_reader_plugin');

export function examplePlugin(frame: Frame): string {
  'worklet';

  if (plugin == null) throw new Error('Failed to load Frame Processor Plugin "qr_reader_plugin"!');

  return plugin.call(frame) as string;
}
