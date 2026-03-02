import styles from "./style.module.css";
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from "recharts";

function MyLineChart({ data, xKey, yKey, height }) {
  return (
    <ResponsiveContainer width="100%" height={height}>
      <LineChart data={data} margin={{ top: 5, right: 10, left: 10, bottom: 5 }}>
        <CartesianGrid strokeDasharray="3 3" vertical={false}/>
        <XAxis dataKey={xKey} tick={{ className: 'caption'}} />
        <YAxis 
          hide 
          domain={([dataMin, dataMax]) => {
            const range = dataMax - dataMin;
            const padding = range === 0 ? 1 : range * 0.05; 
            
            return [dataMin - padding, dataMax + padding];
          }}
        />
        <Tooltip 
          contentStyle={{ 
            border: 'none', 
            backgroundColor: 'var(--color-gray-6-transparent)', 
            borderRadius: '5px',
            display: 'flex',
            flexDirection: 'column',
            padding: '5px 10px'
          }}
          itemStyle={{ color: 'var(--color-black)', padding: '0px'}}
          labelStyle={{ color: 'var(--color-black)', padding: '0px' }}
        />
        <Line 
          type="linear" 
          dataKey={yKey}
          stroke="var(--color-gray-3)" 
          strokeWidth={2} 
          dot={{ r: 2, fill: 'var(--color-gray-3)' }}
          activeDot={{ r: 4, fill: 'var(--color-gray-1)' }}
        />
      </LineChart>
    </ResponsiveContainer>
  );
}

export default MyLineChart;
